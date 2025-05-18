package com.amin.ameenserver.auth;

import com.amin.ameenserver.JwtProvider;
import com.amin.ameenserver.user.User;
import com.amin.ameenserver.user.UserMapper;
import com.amin.ameenserver.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    /*@PostMapping("/api/v1/auth/login")
    public ResponseEntity<UserDto> authenticateUser(HttpServletRequest req, @RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.userName, loginDto.password));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        HttpSession session = req.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        User user = userRepository.findByUserName(authentication.getName());

        UserDto userDto = UserMapper.userToUserDto(user);

        return ResponseEntity.ok().body(userDto);
    }*/

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken;
            authenticationToken = new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword(), Arrays.asList(new SimpleGrantedAuthority(request.getRole())));
            Authentication authenticate = authenticationManager.authenticate(authenticationToken); 

            CostumeUserDetails userdetails = authenticate.getPrincipal())
            User user = userdetails.getUser();

            String token = jwtProvider.generateAccessToken(user);

            LoginResponse loginResponse = new LoginResponse(token, UserMapper.userToUserDto(user));
            return ResponseEntity.ok()
                    .header( HttpHeaders.AUTHORIZATION, "Bearer "+token )
                    .body(loginResponse);

        } catch (BadCredentialsException ex) {
            log.error("login error: "+ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<?> logout() {
        authenticationManager.authenticate(null);
        return ResponseEntity.ok().build();
    }
}
