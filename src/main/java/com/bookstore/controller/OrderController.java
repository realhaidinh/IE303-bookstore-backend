package com.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.model.Book;
import com.bookstore.model.Order;
import com.bookstore.model.User;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.UserRepository;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.PostMapping;

@RequestMapping("/api/order")
@RestController
public class OrderController {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;

    @GetMapping("")
    public List<Order> findAllOrder() {
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOrderById(@PathVariable("id") String id) {
        try {
            Order order = orderRepository.findById(id).get();
            return new ResponseEntity<>(order, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> createOrder(@RequestBody String shippingAddress) {
        try {
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = userRepository.findByUsername(authentication.getName());
            var cart = user.getCart();
            Order order = new Order(user.getUsername(), cart, shippingAddress);
            for (var item : cart) {
                Book book = bookRepository.findById(item.getItemId()).get();
                if (book.getStock() < item.getQuantity()) {
                    return new ResponseEntity<>(HttpStatusCode.valueOf(400));
                }
                var price = book.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                order.setTotalPrice(order.getTotalPrice().add(price));
            }
            user.setCart(null);
            userRepository.save(user);
            orderRepository.save(order);
            return new ResponseEntity<>(order, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("id") String id) {
        orderRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable("id") String id, @RequestBody String status) {
        try {
            Order order = orderRepository.findById(id).get();
            if (order.getOrderStatus().equals("Đã giao")
                    || order.getOrderStatus().equals("Đã hủy")) {
                return new ResponseEntity<>(HttpStatusCode.valueOf(400));
            }
            order.setOrderStatus(status);
            if (status.equals("Đã giao")) {
                var items = order.getOrderItems();
                for (var item : items) {
                    Book book = bookRepository.findById(item.getItemId()).get();
                    if (book.getStock() < item.getQuantity()) {
                        return new ResponseEntity<>(HttpStatusCode.valueOf(400));
                    }
                    book.setStock(book.getStock() - item.getQuantity());
                    book.setSoldQty(book.getSoldQty() + item.getQuantity());
                }
            }
            orderRepository.save(order);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }

}
