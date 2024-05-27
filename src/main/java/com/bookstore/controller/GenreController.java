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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/genre")
/**
 * Controller quản lý thể loại sách
 */
public class GenreController {
    @Autowired
    GenreRepository genreRepository;
    @GetMapping()
    /**
     * Tìm tất cả các thể loại sách
     * @return Danh sách toàn bộ thể loại sách
     */
    public List<Genre> findAllGenre() {
        return genreRepository.findAll();
    }
    @GetMapping("/{name}")
    /**
     * Tìm thể loại sách theo tên thể loại
     * @param name tên thể loại sách
     * @return thể loại sách
     */
    public Genre findGenreByName(@PathVariable("name") String name) {
        return genreRepository.findByName(name);
    }
    
    @PostMapping()
    /**
     * Tạo thể loại sách mới
     * @param genre thông tin thể loại sách
     * @return Http status 201 và thể loại sách nếu thành công, ngược lại trả về 400
     */
    public ResponseEntity<?> createGenre(@RequestBody Genre genre) {
        try {
            Genre savedGenre = genreRepository.save(genre);
            return new ResponseEntity<>(savedGenre, HttpStatusCode.valueOf(201));
            
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatusCode.valueOf(400));
        }
    }
    @DeleteMapping()
    /**
     * 
     * @param id id thể loại sách
     * @return Http status 200 nếu thành công, ngược lại 400 nếu thất bại
     */
    public ResponseEntity<?> deleteGenre(@RequestParam(name = "id", required = true) String id) {
        try {
            genreRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
            
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(400));
        }
    }
    @PatchMapping()
    /**
     * 
     * @param id id thể loại sách
     * @param newGenre thông tin sách cập nhật
     * @return Http status 201 và thể loại sách nếu thành công, ngược lại trả về 400
     */
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
