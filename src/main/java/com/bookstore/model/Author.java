package com.bookstore.model;

import org.springframework.data.annotation.Id;

public class Author {
    @Id
    String id;
    String name;
    String information;
    String image;
}
