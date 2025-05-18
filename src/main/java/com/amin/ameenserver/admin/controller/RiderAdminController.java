package com.amin.ameenserver.admin;

import com.amin.ameenserver.user.Rider;
import com.amin.ameenserver.user.RiderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RiderAdminController {

    @Autowired
    private RiderRepository riderRepository;

    @PostMapping("/admin/users/riders/")
    public Rider createUserRider(Rider rider){
        return riderRepository.save(rider);
    }
}
