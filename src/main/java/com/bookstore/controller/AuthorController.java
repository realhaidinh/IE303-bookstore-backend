package com.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bookstore.repository.AuthorRepository;
import com.bookstore.service.FileStorageService;
import com.bookstore.model.Author;
import com.bookstore.model.AuthorForm;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("api/author")
public class AuthorController {
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    FileStorageService fileStorageService;
    @GetMapping()
    public Author findAuthorByName(@RequestParam("name") String name) {
        return authorRepository.findByName(name);
    }
    @GetMapping()
    
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
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
    @DeleteMapping()
    public ResponseEntity<?> deleteAuthor(@RequestParam(value = "id") String id) {
        try {
            Author author = authorRepository.findById(id).get();
            authorRepository.deleteById(id);
            fileStorageService.deleteFile(author.getImage());
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }
    @PatchMapping()
    public ResponseEntity<?> updateAuthor(@RequestParam(value = "id") String id, @ModelAttribute AuthorForm authorForm, @RequestParam("image") MultipartFile file) {
        try {
            Author author = authorRepository.findById(id).get();
            fileStorageService.saveFile(file);
            author.setImage(file.getOriginalFilename());
            var result = authorRepository.save(author);
            return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }
}
