package com.bookstore.controller;
import com.bookstore.CustomUserDetails;
import com.bookstore.model.LoginRequest;
import com.bookstore.model.LoginResponse;
import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if(user == null) {
            return new ResponseEntity<LoginResponse>(HttpStatusCode.valueOf(404));
        }
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
      
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtService.generateToken((CustomUserDetails) auth.getPrincipal());
        return new ResponseEntity<LoginResponse>(new LoginResponse(jwt), HttpStatusCode.valueOf(200));
    }
    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody User user) {
        User registerUser = new User();
        registerUser.setUsername(user.getUsername());
        registerUser.setPassword(passwordEncoder.encode(user.getPassword()));
        registerUser.setRole("ROLE_USER");
        registerUser.setImage(user.getImage());
        User savedUser = userRepository.save(registerUser);
        if(savedUser == null) {
            return new ResponseEntity<User>(HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<User>(savedUser, HttpStatusCode.valueOf(200));
    }
    @PatchMapping("/update")
    public ResponseEntity<LoginResponse> update(@RequestBody User updateUser) { 
        User user = userRepository.findByUsername(updateUser.getUsername());
        if(user == null) {
            return new ResponseEntity<LoginResponse>(HttpStatusCode.valueOf(404));
        }
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(updateUser.getUsername(), updateUser.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtService.generateToken((CustomUserDetails) auth.getPrincipal());
        user.setImage(updateUser.getImage());
        user.setPassword(updateUser.getPassword());
        return new ResponseEntity<LoginResponse>(new LoginResponse(jwt), HttpStatusCode.valueOf(201));
    }
}
