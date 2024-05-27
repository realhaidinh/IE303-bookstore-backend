package com.bookstore.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import java.util.List;
import java.util.ArrayList;

public class User {
    @Id
    String id;
    @Indexed(unique = true)
    String username;
    String password;
    String role;
    List<BoughtInformation> cart;
    
    public User() {
        cart = new ArrayList<>();
    }

    public List<BoughtInformation> getCart() {
        return cart;
    }
    public void setCart(List<BoughtInformation> cart) {
        this.cart = cart;
    }
    public String getId() { 
        return id;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
