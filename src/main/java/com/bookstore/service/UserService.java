package com.bookstore.service;

import org.springframework.stereotype.Service;

import com.bookstore.CustomUserDetails;
import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
/**
 * Service thông tin người dùng
 */
public class UserService implements UserDetailsService{
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user == null) 
            throw new UsernameNotFoundException(username);
        return new CustomUserDetails(user);
    }
}
