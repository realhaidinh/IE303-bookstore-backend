package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.Recommender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
public class RecommenderController {

    @Autowired
    private Recommender recommender;

    @PostMapping()
    public List<Book> recommendBooks(@RequestBody Book targetBook, @RequestParam int numberOfRecommendations) {
        
        return recommender.recommend(targetBook, numberOfRecommendations);
    }
}
