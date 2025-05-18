package com.amin.ameenserver.admin;

import com.amin.ameenserver.user.User;
import com.amin.ameenserver.user.UserRepository;
import com.amin.ameenserver.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminUsersController {

    @Autowired
    private UserService userService;


    @GetMapping("/users/{id}")
    public String getUser(@PathVariable Long id, Model model){
        model.addAttribute("users", userService.getUser(id));
        return "admin/userShowUser";
    }

    @GetMapping("/users")
    public String getUsers(@RequestParam(defaultValue = "1" int page, Model model){
        List<User> users = userService.getAllUser(page);
        model.addAttribute("users", users);
        return "admin/usersList";
    }

    @GetMapping("/users/create")
    public String createForm(Model model){
        User user = new User();
        model.addAttribute("user", user);
        return "admin/usersCreate";
    }

    
    @GetMapping("/users/userdetails")
    public List<UserDetails> getUserDetails(){
        return userDetailsRepository.findAll();
    }

    @GetMapping("/users/userdetails/{id}")
    public UserDetails getUserDetail(@PathVariable Long id){
        return userDetailsRepository.findById(id).orElseThrow();
    }

    @PostMapping("/users/userdetails")
    public UserDetails createUserDetails(UserDetails userDetails){
        return userDetailsRepository.save(userDetails);
    }

    @PostMapping("/users/userdetails/delete")
    public void deleteUserDetails(UserDetails userDetails){
        userDetailsRepository.delete(userDetails);
    }

    @PostMapping("/users/userdetails/update")
    public UserDetails updateUserDetails(UserDetails userDetails){
        return userDetailsRepository.save(userDetails);
    }

@PostMapping("/users/roles")
    public String create(@RequestParam String name){
        Role role = new Role(name, true);
        roleRepository.save(role);
        return "redirect:admin/roles";
    }

    @PostMapping("/users/roles/{id}")
    public String update(@PathVariable String id, @RequestParam String name){
        Role role = roleRepository.findById(id).orElseThrow();
        role.setId(name);
        roleRepository.save(role);
        return "redirect:admin/roles"; 
    }

    @GetMapping("/users/roles/{id}")
    public String getRole(@PathVariable String ID, Model model ){
        Role role = roleRepository.findById(id).orElseThrow();
        model.addAttribute("roles", role);
        return "admin/role";
    }

    @GetMapping("/users/roles")
    public List<Role> getAllRoles(Model model){
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/roles";
    }

    @PostMapping("/users/roles/{id}/delete")
    public String delete(@PathVariable String id){
        roleRepository.deleteById(id);
        return "redirect:admin/roles";
    }

@PostMapping("/admin/users/cars/")
    public UserCar addUserCar(UserCar userCar){
        return userCarRepository.save(userCar);
    }

    @PostMapping("/admin/users/cars/delete")
    public void deleteUserCar(Long id){
        userCarRepository.deleteById(id);
    }

    @GetMapping("/admin/users/cars/create")
    public String CreateUserCar(){
        return "addUserCar";
    }

}
