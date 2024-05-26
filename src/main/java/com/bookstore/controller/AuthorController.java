package com.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bookstore.repository.AuthorRepository;
import com.bookstore.service.FileStorageService;
import com.bookstore.model.Author;
import com.bookstore.model.AuthorForm;
public class AuthorController {
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    FileStorageService fileStorageService;
    @PostMapping(path = "/api/author", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createAuthor(@ModelAttribute AuthorForm authorForm, @RequestParam("image") MultipartFile file) {
        try {
            Author author = new Author(authorForm);
            fileStorageService.saveFile(file);
            author.setImage(file.getOriginalFilename());
            authorRepository.save(author);
            return new ResponseEntity<>(author, HttpStatusCode.valueOf(201));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }
    @DeleteMapping(path = "/api/author")
    public ResponseEntity<?> deleteAuthor(@RequestParam(value = "id") String id) {
        Author author = authorRepository.findById(id).get();
        try {
            authorRepository.deleteById(id);
            fileStorageService.deleteFile(author.getImage());
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }
}
