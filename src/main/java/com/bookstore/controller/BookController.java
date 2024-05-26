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

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("api/book")
public class BookController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findBookById(@PathVariable("id") String id) {
        try {
            Book book = bookRepository.findById(id).get();
            return new ResponseEntity<>(book, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }

    @GetMapping()
    public Page<Book> findBookByQuery(@RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "by", required = false, defaultValue = "id") String field,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "sort", required = false, defaultValue = "ASC") String sort) {
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by(field).ascending();
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by(field).descending();
        }
        Pageable page = PageRequest.of(pageNumber, pageSize, sortable);
        if (author != null) {
            return bookRepository.findByAuthor(author, page);
        }
        if (genre != null) {
            return bookRepository.findByGenre(genre, page);
        }
        return bookRepository.findAll(page);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createBook(@ModelAttribute BookForm bookForm,
            @RequestParam("images") MultipartFile[] files) {
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

    @DeleteMapping()
    public ResponseEntity<?> deleteBook(@RequestParam("id") String id) {
        try {
            Book book = bookRepository.findById(id).get();
            var images = book.getImages();
            for (var image : images) {
                fileStorageService.deleteFile(image);
            }
            bookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }

    @PatchMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updateBook(@RequestParam(value = "id", required = true) String id,
            @ModelAttribute BookForm bookForm, @RequestParam(value = "images", required = false) MultipartFile[] files) {
        try {
            Book book = bookRepository.findById(id).get();
            if (files != null) {
                List<String> images = new ArrayList<>();
                for (MultipartFile file : files) {
                    fileStorageService.saveFile(file);
                    images.add(file.getOriginalFilename());
                }
                book.setImages(images);
            }
            book.set(bookForm);
            Book result = bookRepository.save(book);
            return new ResponseEntity<Book>(result, HttpStatusCode.valueOf(201));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }
}
