package com.bookstore.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.bookstore.model.Book;
import com.bookstore.model.BookForm;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.FileStorageService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;

@RestController
public class BookController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @PostMapping(path = "/api/book", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createBook(@ModelAttribute BookForm bookForm, @RequestParam("images") MultipartFile[] files) {
        try {
            List<String> images = new ArrayList<>();
            for (MultipartFile file : files) {
                fileStorageService.saveFile(file);
                images.add(file.getOriginalFilename());
            }
            Book book = new Book(bookForm);
            book.setImages(images);
            Book result = bookRepository.save(book);
            return new ResponseEntity<Book>(result, HttpStatusCode.valueOf(201));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }
    
}
