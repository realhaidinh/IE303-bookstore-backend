package com.bookstore.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.bookstore.model.Genre;
import com.bookstore.repository.GenreRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("api/genre")
public class GenreController {
    @Autowired
    GenreRepository genreRepository;
    @GetMapping()
    public List<Genre> findAllGenre() {
        return genreRepository.findAll();
    }
    @GetMapping()
    public Genre findGenreByName(@RequestParam(name = "name") String name) {
        return genreRepository.findByName(name);
    }
    
    @PostMapping()
    public ResponseEntity<?> createGenre(@RequestBody Genre genre) {
        try {
            Genre savedGenre = genreRepository.save(genre);
            return new ResponseEntity<>(savedGenre, HttpStatusCode.valueOf(201));
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatusCode.valueOf(400));
        }
    }
    @DeleteMapping()
    public ResponseEntity<?> deleteGenre(@RequestParam(name = "id", required = true) String id) {
        genreRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatusCode.valueOf(200));
    }
    @PatchMapping()
    public ResponseEntity<?> updateGenre(@RequestParam(name = "id", required = true) String id, @RequestBody Genre newGenre) {
        try {
            Genre genre = genreRepository.findById(id).get();
            genre.setName(newGenre.getName());
            genre.setDescription(newGenre.getDescription());
            Genre result = genreRepository.save(genre);
            return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatusCode.valueOf(400));
        }
    }
}
