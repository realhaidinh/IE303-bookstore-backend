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

@RequestMapping("/api/auth")
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

    public static final String defaultAvatar = "public/avatar/default.png";
    @PostMapping("/login")
    /**
     * Thực hiện việc đăng nhập
     * @param loginRequest thông tin tài khoản đăng nhập
     * @return Http response 404 nếu không tìm thấy tài khoản, ngược lại trả về token của người dùng
     */
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user == null) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtService.generateToken((CustomUserDetails) auth.getPrincipal());
        return new ResponseEntity<LoginResponse>(
                new LoginResponse(jwt),
                HttpStatusCode.valueOf(200));
    }

    @PostMapping("/signup")
    /**
     * Đăng ký tài khoản
     * @param user thông tin người dùng muốn đăng ký tài khoản
     * @return Trả về Http response 200 và thông tin người dùng sau khi đăng ký thành công
     */
    public ResponseEntity<User> signup(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
        User registerUser = new User();
        registerUser.setUsername(user.getUsername());
        registerUser.setPassword(passwordEncoder.encode(user.getPassword()));
        registerUser.setRole("ROLE_USER");
        User savedUser = userRepository.save(registerUser);
        if (savedUser == null) {
            return new ResponseEntity<User>(HttpStatusCode.valueOf(500));
        }
        return new ResponseEntity<User>(savedUser, HttpStatusCode.valueOf(200));
    }

    @PatchMapping("/update")
    /**
     * Cập nhật thông tin tài khoản
     * @param updateUser Thông tin người dùng mới được gửi đến
     * @return HTTP status response 200 nếu thành công, ngược lại trả về 400
     */
    public ResponseEntity<?> update(@RequestBody User updateUser) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());
        if (!updateUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updateUser.getPassword()));
        }
        try {
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }
}
