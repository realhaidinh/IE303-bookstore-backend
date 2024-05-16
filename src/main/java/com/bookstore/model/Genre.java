package com.bookstore.model;

import org.springframework.data.annotation.Id;

public class Genre {
    @Id
    String id;
    String name;
    String description;
}
