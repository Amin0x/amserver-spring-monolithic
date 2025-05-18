package com.amin.ameenserver.user;

import com.amin.ameenserver.KafkaConfig;
import com.amin.ameenserver.core.ResourceNotFoundException;
import com.amin.ameenserver.location.Location;
import com.amin.ameenserver.location.LocationRepository;
import com.amin.ameenserver.location.UpdateUserLocationDto;
import com.amin.ameenserver.order.*;
import com.amin.ameenserver.wallet.Account;
import com.amin.ameenserver.wallet.AccountsType;
import com.amin.ameenserver.wallet.CreditAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

@Service
public class UserService {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, Object> template;
    private final CreditAccountRepository creditAccountRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final RiderRepository riderRepository;
    private final DriverRepository driverRepository;
    private final OrderRepository orderRepository;
    private final ValidationCodeRepository validationCodeRepository;
    private final BidRepository bidRepository;

    @Autowired
    public UserService(LocationRepository locationRepository, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, OrderRepository orderRepository, KafkaTemplate<String, Object> template, CreditAccountRepository creditAccountRepository, UserDetailsRepository userDetailsRepository, RiderRepository riderRepository, DriverRepository driverRepository, ValidationCodeRepository validationCodeRepository, BidRepository bidRepository) {
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.template = template;
        this.creditAccountRepository = creditAccountRepository;
        this.userDetailsRepository = userDetailsRepository;
        this.riderRepository = riderRepository;
        this.driverRepository = driverRepository;
        this.orderRepository = orderRepository;
        this.validationCodeRepository = validationCodeRepository;
        this.bidRepository = bidRepository;
    }


    public Location updateUserLocation(UpdateUserLocationDto locationDto, User user, Authentication authentication) {

        Order order = getLastOrder(user.getId(), authentication);

        Location location;
        if (Objects.equals(order.getOrderStatus(), Order.ORDER_STATUS_COMPLETED)){
            //save the location to database
            location = new Location(locationDto.getUserId(), locationDto.getLatitude(),
                    locationDto.getLongitude(), new Date(), null, user);
        } else {
            //save the location to database
            location = new Location(locationDto.getUserId(), locationDto.getLatitude(),
                    locationDto.getLongitude(), new Date(), order, user);
        }

        Location savedLocation = locationRepository.save(location);

        user.setLongitude(locationDto.getLatitude());
        user.setLatitude(locationDto.getLatitude());
        user.setGeohashLocation(locationDto.getGeoHash());
        userRepository.save(user);

        
        template.send(KafkaConfig.TOPIC_USERS_LOCATION_CHANGED,user.getUsername(), savedLocation);

        return savedLocation;
    }

    public Order getLastOrder(long userId, Authentication authentication) {
        final User user = this.getAuthUser();
        /*List<String> status = List.of(Order.ORDER_STATUS_ACCEPTED, Order.ORDER_STATUS_STARTED, Order.ORDER_STATUS_ARRIVED);
        Order order = orderRepository.findFirstByDriverIdOrRiderIdAndStatusInOrderByCreatedAsc(user.getId(), user.getId(), status);*/

        if (user.getDriverMode()){
            return orderRepository.findFirstByDriverIdOrderByCreated(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(""));
        } else {
            return orderRepository.findFirstByRiderIdOrderByCreated(user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(""));
        }
    }

    private boolean isSameUser(User user, Authentication authentication) {
        User authUser = getAuthUser();
        if(authUser == null || user == null)
            return false;

        return user.getId().equals(authUser.getId());
    }

    public User createUser(String username, String password, String name, String email, MultipartFile img){

        Role roles = roleRepository.findById(Role.USER_RIDER).orElseThrow();
        String userImg = null;

        if (img != null) {
            userImg = UUID.randomUUID().toString().replace("-", "");
            if (img.getOriginalFilename() != null) {
                String ext = img.getOriginalFilename().substring(img.getOriginalFilename().lastIndexOf('.'));
                userImg = userImg + ext;
            }
            try {
                Path filename = Paths.get("uploads/profiles").resolve(userImg);
                img.transferTo(filename);
            } catch (IOException e) {
                throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
            }
        }

        User user = new User();
        user.setLongitude(null);
        user.setLatitude(null);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setEmail(email);
        user.setRegistrationDate(new Date());
        user.setRoles(Collections.singleton(roles));
        user.setStatus("active");
        user.setEnabled(true);
        user.setDriverMode(false);
        user.setProfileImage(userImg);
        user.setRememberToken(null);
        user.setVerified(false);
        user.setCreated(new Date());
        user.setUpdated(new Date());
        user.setGeohashLocation(null);

        User newUser = userRepository.save(user);

        Rider rider = new Rider(newUser, newUser.getName(), newUser.getName(), 0, 0 ,0 , 0);
        riderRepository.save(rider);

        UserDetails userDetails = new UserDetails( name, username, false,
                new Date(), null, 1, newUser);

        userDetailsRepository.save(userDetails);

        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        Account account = new Account(newUser, uuid,
                AccountsType.USER, 0, true, new Date());

        creditAccountRepository.save(account);


        return newUser;
    }

    public Driver createUserDriver(User user,
                                   String drivingLicenceNumber,
                                   Date drivingLicenceIssueDate,
                                   Date drivingLicenceExpireDate,
                                   MultipartFile drivingLicenceImage,
                                   Date governmentIdIssueDate,
                                   Date governmentIdExpireDate,
                                   MultipartFile governmentIdImage){

        if (driverRepository.existsById(user.getId()))
            throw new EntityExistsException("user already registered as driver");

        Driver driver = new Driver();

        if (drivingLicenceImage.getOriginalFilename() != null) {
            String ext = drivingLicenceImage.getOriginalFilename().substring(drivingLicenceImage.getOriginalFilename().lastIndexOf("."));
            Path uploads = Paths.get("uploads").resolve("driving_licence-" + user.getId() + ext);

            try {
                drivingLicenceImage.transferTo(uploads);
                driver.setDrivingLicenceImage(uploads.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (governmentIdImage.getOriginalFilename() != null){
            String ext = governmentIdImage.getOriginalFilename().substring(governmentIdImage.getOriginalFilename().lastIndexOf("."));
            Path uploads = Paths.get("uploads").resolve("government_Id-" + user.getId() + ext);

            try {
                governmentIdImage.transferTo(uploads);
                driver.setGovernmentIdImage(uploads.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        driver.setUser(user);
        driver.setActive(false);
        driver.setEnabledDriver(false);
        driver.setAc(false);
        driver.setCharger(false);
        driver.setRideCount(0);
        driver.setLikes(0);
        driver.setPoints(0);
        driver.setRideAcceptedCount(0);
        driver.setDrivingLicenceExpireDate(drivingLicenceExpireDate);
        driver.setDrivingLicenceIssueDate(drivingLicenceIssueDate);
        driver.setDrivingLicenceImage(null);
        driver.setDrivingLicenceNumber(drivingLicenceNumber);
        driver.setGovernmentIdExpireDate(governmentIdExpireDate);
        driver.setGovernmentIdImage(null);
        driver.setGovernmentIdIssueDate(governmentIdIssueDate);

        //add user driver role
        Role role = roleRepository.findById(Role.USER_DRIVER).orElseThrow();
        user.getRoles().add(role);
        userRepository.save(user);

        return driverRepository.save(driver);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(""));
    }

    public Optional<User> getUserById(Long id){

        return userRepository.findById(id);
    }

    public List<User> getAllUsers(int page, int size, String sortBy, String sortDirection) {
    List<User> users = new ArrayList<>();

    if (page < 1) {
        page = 1;
    }

    if (size < 1) {
        size = 10; // Default size
    }

    Sort.Direction direction = Sort.Direction.ASC;
    if (sortDirection.equalsIgnoreCase("DESC")) {
        direction = Sort.Direction.DESC;
    }

    PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(direction, sortBy));
    userRepository.findAll(pageRequest).forEach(users::add);

    return users;
}


    public void updateUserProfileImage(Authentication id, MultipartFile img){

    }

    public void blockUser(User user, Date until){
        user.setEnabled(false);
        user.setBlockedUntil(until);
        userRepository.save(user);
    }

    public void disableUser(Long id, String reason){
        Optional<User> user = this.getUserById(id);
        if(user.isPresent()){
            user.setEnabled(false);
            userRepository.save(user);
            return;
        }
        //else throw exception 
        throw new Illegalargumentexception();
    }

    public void enableUser(Long id){
        Optional<User> user = this.getUserById(id);
        if(user.isPresent()){
            user.setEnabled(false);
            userRepository.save(user);
            return;
        }
        //else throw exception 
        throw new Illegalargumentexception();
    }

    public void verifyPhone(long id, String code, String phone){

        List<ValidationCode> list = validationCodeRepository.findByPhoneAndCodeOrderByTime(code, phone, new Date(Instant.now().minusSeconds(60*40).getNano()));
        //get all code in valid time range

        for (ValidationCode codeItem : list) {
            if (codeItem.equals(code)) {
                return;
            }
        }

    }

    public void sendPhoneVerifiecationCode(String phone){
        ValidationCode validationCode = new ValidationCode();
        validationCode.setDate(new Date());
        validationCode.setCode("");
        validationCode.setPhone(phone);

        validationCodeRepository.save(validationCode);
    }

    public void verifyEmail(long id, String verificationCode, String email){
        User user = this.getUser(id);

        user.setVerified(true);
        userRepository.save(user);
    }

    public void changeDriverMode(long id, boolean driverMode){
        Optional<User> user = this.getUserById(id);
        if(user.isPresent()){
            user.setDriverMode(driverMode);
            userRepository.save(user);
        } else {
            throw new Illegalargumentexception();
        }
    }

    public boolean isAdmin(User user) {
        if (user == null) {
            throw new IllegalArgumentException("user is null");
        }

        return user.getRoles().stream().anyMatch(role -> role.getId().equals("ADMIN"));
    }

    public User getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User)
            return (User) authentication.getPrincipal();


        throw new IllegalArgumentException("");
    }

    public UserProfileDto getUserProfile(User user) {
        UserProfileDto profileDto = new UserProfileDto();
        profileDto.setName(user.getName());
        profileDto.setPhoneNumber(user.getUsername());
        profileDto.setEmail(user.getEmail());
        profileDto.setImage(user.getProfileImage()); // todo: driver or rider imgs not same

        if (user.getDriverMode()) {
            Driver driver = this.getUserDriver(user.getId());
            profileDto.setLikes(driver.getLikes());
            profileDto.setPoints(driver.getPoints());

            Long rate5 = orderRepository.countByDriverIdAndRiderRate(user.getId(), 5.0f);
            Long rate4 = orderRepository.countByDriverIdAndRiderRate(user.getId(), 4.0f);
            Long rate3 = orderRepository.countByDriverIdAndRiderRate(user.getId(), 3.0f);
            Long rate2 = orderRepository.countByDriverIdAndRiderRate(user.getId(), 2.0f);
            Long rate1 = orderRepository.countByDriverIdAndRiderRate(user.getId(), 1.0f);

            float l = (rate1 + (rate2 * 2.0f) + (rate3 * 3.0f) + (rate4 * 4.0f) + (rate5 * 5.0f)) / (rate1 + rate2 + rate3 + rate4 + rate5);
            profileDto.setRate(Math.round(l));

        } else {
            Rider rider = this.getUserRider(user.getId());
            profileDto.setLikes((int) rider.getLikes());
            profileDto.setPoints((int) rider.getPoints());

            Long rate5 = orderRepository.countByRiderIdAndDriverRate(user.getId(), 5.0f);
            Long rate4 = orderRepository.countByRiderIdAndDriverRate(user.getId(), 4.0f);
            Long rate3 = orderRepository.countByRiderIdAndDriverRate(user.getId(), 3.0f);
            Long rate2 = orderRepository.countByRiderIdAndDriverRate(user.getId(), 2.0f);
            Long rate1 = orderRepository.countByRiderIdAndDriverRate(user.getId(), 1.0f);

            float l = (rate1 + (rate2 * 2.0f) + (rate3 * 3.0f) + (rate4 * 4.0f) + (rate5 * 5.0f)) / (rate1 + rate2 + rate3 + rate4 + rate5);
            profileDto.setRate(Math.round(l));
        }

        return profileDto;
    }

    private Optional<Rider> getUserRider(Long id) {
        return riderRepository.findById(id);
    }

    public boolean switchUserMode(User user, Authentication authentication)

        user.setDriverMode(!user.getDriverMode());
        if (!user.getDriverMode()) {
            user.getDriver().setActive(false);
        }

        userRepository.save(user);
        return true;
    }

    public Optional<Bid> findBid(User user, Order order) {
        return bidRepository.findByUserAndOrder(user.getId(), order.getId());
    }

    public Optional<Driver> getUserDriver(Long id) {
        return driverRepository.findById(id);
    }

    public Driver connectDriver(Long id) {
        Driver user  = this.getUserDriver(id);
        user.setActive(!user.isActive());
        return driverRepository.save(user);
    }
}
