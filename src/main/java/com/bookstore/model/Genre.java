package com.bookstore.model;

import org.springframework.data.annotation.Id;

public class Genre {
    @Id
    String id;
    String name;
    String description;
    
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
