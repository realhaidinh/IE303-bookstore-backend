package com.bookstore.model;

import org.springframework.data.annotation.Id;

public class Author {
    @Id
    String id;
    String name;
    String information;
    String image;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getInformation() {
        return information;
    }
    public void setInformation(String information) {
        this.information = information;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
