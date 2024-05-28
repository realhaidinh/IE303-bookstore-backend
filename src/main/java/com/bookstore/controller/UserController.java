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


@RequestMapping("/api/user")
@RestController
/**
 * Controller quản lý tài khoản người dùng
 */
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
    /**
     * Lấy thông tin người dùng
     * @return thông tin người dùng
     */
    public Profile getUserProfile() {
        //Tìm người dùng trong spring security dựa theo token được gửi đến
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
    /**
     * Tìm giỏ hàng của người dùng
     * @return giỏ hàng
     */
    public List<BoughtInformation> findCart() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var authUser = (CustomUserDetails) auth.getPrincipal();
        User user = userRepository.findByUsername(authUser.getUsername());
        return user.getCart();
    }

    @GetMapping("/order")
    /**
     * Tìm hóa đơn của người dùng
     * @return danh sách hóa đơn
     */
    public List<Order> findAllUserOrder() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var authUser = (CustomUserDetails) auth.getPrincipal();
        return orderRepository.findByUsername(authUser.getUsername());
    }

    @PostMapping("/cart")
    /**
     * Cập nhật giỏ hàng
     * @param newCart giỏ hàng mới
     * @return Http status 200 nếu thành công, ngược lại trả về 400
     */
    public ResponseEntity<?> updateCart(@RequestBody List<BoughtInformation> newCart) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var authUser = (CustomUserDetails) auth.getPrincipal();
        User user = userRepository.findByUsername(authUser.getUsername());
        try {
            for(var item : newCart) {
                Book book = bookRepository.findById(item.getItemId()).get();
                if(book.getStock() < item.getQuantity()) {
                    return new ResponseEntity<>(HttpStatusCode.valueOf(400));
                }
                item.setTitle(book.getTitle());
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
    /** 
     * Tìm tất cả tài khoản
     * @return danh sách tất cả tài khoản
     */
    @GetMapping("all")
    public List<User> findAllUser() {
        return userRepository.findAll();
    }
    
}
