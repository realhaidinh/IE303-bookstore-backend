package com.bookstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
/**
 * Controller quản lý Tác giả
 */
public class AuthorController {
    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    FileStorageService fileStorageService;
    @GetMapping("")
    public ResponseEntity<?> findAllAuthor() {
        return new ResponseEntity<>(authorRepository.findAll(), HttpStatusCode.valueOf(200));
    }
    
    @GetMapping("/{name}")
    /**
     * Tìm tác giả theo tên
     * @param name tên tác giả cần tìm
     * @return tác giả 
     */
    public Author findAuthorByName(@PathVariable("name") String name) {
        return authorRepository.findByName(name);
    }
    
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    /**
     * Tạo tác giả mới
     * @param authorForm thông tin tác giả muốn khởi tạo
     * @param file ảnh tác giả
     * @return Http status 400 nếu không thành công, ngược lại trả về status 200 và tác giả vừa tạo
     */
    public ResponseEntity<?> createAuthor(@ModelAttribute AuthorForm authorForm, @RequestParam("image") MultipartFile file) {
        try {
            Author author = new Author(authorForm);
            fileStorageService.saveFile(file);
            author.setImage(file.getOriginalFilename());
            authorRepository.save(author);
            return new ResponseEntity<>(author, HttpStatusCode.valueOf(201));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }
    @DeleteMapping()
    /**
     * Xóa tác giả theo id
     * @param id id tác giả
     * @return Http status 200 nếu thành công, ngược lại trả về 400
     */
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
    /**
     * Cập nhật thông tin tác giả
     * @param id id tác giả
     * @param authorForm thông tin tác giả
     * @param file ảnh tác giả
     * @return Http status 200 nếu thành công, ngược lại trả về 400
     */
    public ResponseEntity<?> updateAuthor(@RequestParam(value = "id") String id, @ModelAttribute AuthorForm authorForm, @RequestParam("image") MultipartFile file) {
        try {
            Author author = authorRepository.findById(id).get();
            fileStorageService.saveFile(file);
            author.setImage(file.getOriginalFilename());
            var result = authorRepository.save(author);
            return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }
}
