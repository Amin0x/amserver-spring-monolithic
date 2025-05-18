package com.amin.ameenserver;

import com.amin.ameenserver.user.User;
import com.amin.ameenserver.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public CustomUserDetailsService() {
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("CustomUserDetailsService::loadUserByUsername: username= {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username:" + username));

        /*UserDetails user1;
        user1 = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password("")
                .authorities(user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getId())).toList())
                .build();

        return user1;*/

        return user;
    }
}
