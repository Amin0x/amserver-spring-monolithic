package com.amin.ameenserver.user;

import com.amin.ameenserver.core.ResourceNotFoundException;
import com.amin.ameenserver.location.Location;
import com.amin.ameenserver.location.UpdateUserLocationDto;
import com.amin.ameenserver.order.Bid;
import com.amin.ameenserver.order.Order;
import com.amin.ameenserver.order.OrderRepository;
import com.amin.ameenserver.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class UserController {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final OrderService orderService;
    private final UserService userService;


    @Autowired
    public UserController(KafkaTemplate<String, Object> kafkaTemplate, RedisTemplate<String, Object> redisTemplate, PasswordEncoder passwordEncoder, OrderService orderService, UserService userService) {
        this.kafkaTemplate = kafkaTemplate;
        this.redisTemplate = redisTemplate;
        this.passwordEncoder = passwordEncoder;
        this.orderService = orderService;
        this.userService = userService;
    }


    @GetMapping("/api/v1/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id){
        Optional<User> user = userService.getUserById(id);
        if (!user.isPresent())
            return new ResponseEntity(null, HttpStatus.METHOD_NOT_ALLOWED);

        //todo: chech if authenticated user is authorized 

        return new ResponseEntity(user, HttpStatus.OK);
    }

    
    @GetMapping("/api/v1/users/getUserProfile")
    public ResponseEntity<UserProfileDto> getUserProfile(){
        User user = userService.getAuthUser();
        UserProfileDto profile = userService.getUserProfile(user);
        return new ResponseEntity(profile, HttpStatus.OK);
    }
    

    @PostMapping("/api/v1/users/updateUserLocation")
    public Location updateUserLocation(Authentication authentication,
                                       @RequestBody UpdateUserLocationDto locationDto){

        Optional<User> user = userService.getUserById(locationDto.getUserId());
        if (!user.isPresent() || !Objects.equals(user.get().getUsername(), authentication.getName()))
            throw new ResourceNotFoundException("");

        return userService.updateUserLocation(locationDto, user.get(), authentication);
    }

    @PostMapping("/api/v1/users")
    public User registerUser(@RequestBody SignUpDto signUpDto){
        
        signUpDto.setUsername(signUpDto.getUsername().trim());
        signUpDto.setEmail(signUpDto.getEmail().trim().toLowerCase());


        if(!StringUtils.hasText(signUpDto.getUsername())){
            log.error("error username empty");
            throw new IllegalArgumentException("error: username empty");
        }

        if (!StringUtils.hasText(signUpDto.getPassword())){
            log.error("error: password is empty");
            throw new IllegalArgumentException("error: password is empty");
        }

        // add check for email exists in DB
        if(StringUtils.hasText(signUpDto.getEmail())){
            if (userRepository.existsByEmail(signUpDto.getEmail())) {
                throw new EntityExistsException("error: email exist");
            }
 
        }

         
        if (userService.userExistsByUsername(signUpDto.getUsername()){
            throw new EntityExistsException("error: user exist");
        }


        User user = userService.createUser(signUpDto);

        return user;
    }

    @PostMapping("/api/v1/users/updateUserProfileImage")
    public void updateUserProfileImage(@RequestPart("image") MultipartFile file, Authentication authentication){
        //create dir if not exist
        //remove old image if found
        //write the new image
        //persist path to db
        userService.updateUserProfileImage(authentication, file);
    }

    @PostMapping("/api/v1/users/{userId}/switchUserMode")
    public ResponseEntity<?> switchUserMode(@PathVariable long userId, Authentication authentication){

        Optional<User> user = userService.getUserById(userId);
        if(!user.isPresent())
            return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);

        return userService.switchUserMode(user.get(), authentication);
    }

    @GetMapping("/api/v1/users/{userId}/orders")
    public ResponseEntity<List<Order>> getOrdersHistory(@PathVariable long userId, @RequestParam(defaultValue = "1") int page, Authentication authentication){

        Optional<User> user = userService.getUserById(userId);
        if(!user.isPresent())
            return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);

        if (!Objects.equals(user.get().getUsername(), authentication.getName())){
            throw new ResourceNotFoundException("user is not authorized to access this resource");
        }

        List<Order> orders = null;
        orders = orderService.getOrdersHistory(user.get(), page);

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/api/v1/users/{userId}/orders/{orderId}/bid")
    public ResponseEntity<Bid> getBid(@PathVariable long userId, @PathVariable long orderId, Authentication authentication){

        Optional<User> user = userService.getUserById(userId);
        if(!user.isPresent())
            return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
        
        Optional<Order> order = orderService.getOrder(orderId) ;
        if(!order.isPresent())
            return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
        
        Optional<Bid> bid = userService.findBid(user.get(), order);
        if(!bid.isPresent())
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(bid.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/api/v1/users/createDriver", produces = "application/json")
    public ResponseEntity<Driver> registerDriver(@Validated @RequestParam Long userId,
                                                 @RequestParam String drivingLicNumber,
                                                 @RequestParam Date drivingLicExpireDate,
                                                 @RequestParam Date drivingLicIssueDate,
                                                 @RequestPart MultipartFile drivingLicImage,
                                                 @RequestParam String governmentIdNumber,
                                                 @RequestParam Date governmentIdExpireDate,
                                                 @RequestParam Date governmentIdIssueDate,
                                                 @RequestPart MultipartFile governmentIdImage,
                                                 Authentication authentication){

        User user = userService.getUserById(userId);

        Driver driver = userService.createUserDriver(user, drivingLicNumber, drivingLicIssueDate,
                drivingLicExpireDate, drivingLicImage, governmentIdIssueDate,
                governmentIdExpireDate, governmentIdImage );

        return new ResponseEntity<>(driver, HttpStatus.OK);
    }

    @PostMapping("/api/v1/users/connectDriver")
    private ResponseEntity<Driver> connectDriver(@RequestBody Long id){
        return new ResponseEntity<>(userService.connectDriver(id), HttpStatus.OK);
    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<?> adminGetUser(@PathVariable Long id){
        Optional<User> user = userService.getUserById(userId);
        if(!user.isPresent())
            return new ResponseEntity<>(null, HttpStatus.METHOD_NOT_ALLOWED);
        

        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> adminGetUsersList(@RequestParam(defaultValue = "0") int page){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAllUsers(page));
    }

    @PostMapping("/admin/users")
    public User adminRegisterUser(@RequestBody SignUpDto signUpDto){
        
        signUpDto.setUsername(signUpDto.getUsername().trim());
        signUpDto.setEmail(signUpDto.getEmail().trim().toLowerCase());


        if(!StringUtils.hasText(signUpDto.getUsername())){
            log.error("error username empty");
            throw new IllegalArgumentException("error: username empty");
        }

        if (!StringUtils.hasText(signUpDto.getPassword())){
            log.error("error: password is empty");
            throw new IllegalArgumentException("error: password is empty");
        }

        // add check for email exists in DB
        if(StringUtils.hasText(signUpDto.getEmail())){
            if (userService.userExistsByEmail(signUpDto.getEmail())) {
                throw new EntityExistsException("error: email exist");
            }
 
        }

         
        if (userService.userExistsByUsername(signUpDto.getUsername()){
            throw new EntityExistsException("error: user exist");
        }


        User user = userService.createUser(signUpDto);

        return user;
    }

    @PostMapping("/admin/users")
    public ResponseEntity<User> adminRegisterUser(@RequestBody User user) {
        // Validate user object (assuming a validateUser method exists)
        validateUser(user);

        User userSaved = userService.createUser(user);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }

    //amin web portal endpoint 
    @GetMapping("/admin/web/users/{id}")
    public String adminGetUser(@PathVariable Long id, Model model){
        Optional<User> user = userService.getUserById(userId);
        if(!user.isPresent())
            return "";

        model.addAttribute("user", user);

        return "";
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
