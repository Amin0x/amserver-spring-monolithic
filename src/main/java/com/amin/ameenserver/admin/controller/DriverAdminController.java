package com.amin.ameenserver.admin;

import com.amin.ameenserver.user.Driver;
import com.amin.ameenserver.user.DriverRepository;
import com.amin.ameenserver.user.UserDriverDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/users/drivers")
public class DriverAdminController {

    @Autowired
    private DriverRepository driverRepository;

    @GetMapping("/{id}")
    public Driver getUserDriver(@RequestParam Long id){
        return driverRepository.findById(id).orElseThrow();
    }

    @GetMapping("/")
    public List<Driver> getUsersDrivers(@RequestParam int page){
        return driverRepository.findAll(Pageable.ofSize(20).withPage(page)).toList();
    }

    @PostMapping("/")
    public Driver createUserDriver(@RequestBody UserDriverDto user){
        //return userService.createUser();

        return null;
    }

    @PostMapping("/update")
    public void updateUserDriver(@RequestBody Driver user){
        driverRepository.save(user);
    }

    @PostMapping("/delete")
    public void deleteUserDriver(@RequestBody Driver user){
        driverRepository.delete(user);
    }
}
