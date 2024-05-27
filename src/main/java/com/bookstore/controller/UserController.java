package com.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.CustomUserDetails;
import com.bookstore.model.BoughtInformation;
import com.bookstore.model.Order;
import com.bookstore.model.User;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.UserRepository;
import com.bookstore.repository.BookRepository;
import com.bookstore.model.Profile;
import com.bookstore.model.Book;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/api/user")
@RestController
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    BookRepository bookRepository;

    @GetMapping("/profile")
    public Profile getUserProfile() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var user = (CustomUserDetails) auth.getPrincipal();
        String role = user.getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();
        return new Profile(user.getUsername(), role);
    }

    @GetMapping("/cart")
    public List<BoughtInformation> findCart(@RequestParam String param) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var authUser = (CustomUserDetails) auth.getPrincipal();
        User user = userRepository.findByUsername(authUser.getUsername());
        return user.getCart();
    }

    @GetMapping("/order")
    public List<Order> findAllUserOrder(@RequestParam String param) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var authUser = (CustomUserDetails) auth.getPrincipal();
        return orderRepository.findByUsername(authUser.getUsername());
    }

    @PostMapping("/cart")
    public ResponseEntity<?> postMethodName(@RequestBody List<BoughtInformation> newCart) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var authUser = (CustomUserDetails) auth.getPrincipal();
        User user = userRepository.findByUsername(authUser.getUsername());
        try {
            for(var item : newCart) {
                Book book = bookRepository.findById(item.getItemId()).get();
                if(book.getStock() < item.getQuantity()) {
                    return new ResponseEntity<>(HttpStatusCode.valueOf(400));
                }
                item.setImage(book.getImages().get(0));
                item.setPrice(book.getPrice());
            }
            user.setCart(newCart);
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }
}
