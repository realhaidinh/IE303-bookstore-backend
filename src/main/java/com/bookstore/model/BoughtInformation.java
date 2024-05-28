package com.bookstore.model;

import java.math.BigDecimal;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

public class BoughtInformation {
    String itemId;
    String title;
    Integer quantity;
    @Field(targetType = FieldType.DECIMAL128)
    BigDecimal price;
    String image;
    public BoughtInformation() {

    }
    public BoughtInformation(Book book, Integer quantity) {
        this.itemId = book.getId();
        this.title = book.getTitle();
        this.price = book.getPrice();
        this.image = book.getImages().get(0);
        this.quantity = quantity;
    }
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
