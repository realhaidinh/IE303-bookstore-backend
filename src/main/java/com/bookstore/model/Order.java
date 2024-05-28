package com.bookstore.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Order {
    @Id
    String id;
    String username;
    @Field(targetType = FieldType.DECIMAL128)
    BigDecimal totalPrice;
    String orderStatus;
    @DateTimeFormat(pattern = "yyyy-MM-dd") 
    Date orderAt;
    List<BoughtInformation> orderItems;
    String shippingAddress;

    public Order() {
        this.totalPrice = new BigDecimal(0);
        this.orderAt = new Date();
        this.orderStatus = "Đang xử lý";
    }
    public Order(String username, List<BoughtInformation> cart, String shippingAddress) {
        this.totalPrice = new BigDecimal(0);
        this.orderAt = new Date();
        this.orderStatus = "Đang xử lý";
        this.username = username;
        this.orderItems = cart;
        this.shippingAddress = shippingAddress;
    }
    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderAt() {
        return orderAt;
    }

    public void setOrderAt(Date orderAt) {
        this.orderAt = orderAt;
    }

    public List<BoughtInformation> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<BoughtInformation> orderItems) {
        this.orderItems = orderItems;
    }
}
