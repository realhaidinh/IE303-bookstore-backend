package com.bookstore.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

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

/**
 * Controller quản lý thể loại sách
 */
@RestController
@RequestMapping("api/genre")
public class GenreController {
    @Autowired
    GenreRepository genreRepository;

    /**
     * Tìm tất cả các thể loại sách
     * 
     * @return Danh sách toàn bộ thể loại sách
     */
    @GetMapping()
    public List<Genre> findAllGenre() {
        return genreRepository.findAll();
    }

    /**
     * Tìm thể loại sách theo tên thể loại
     * 
     * @param name tên thể loại sách
     * @return thể loại sách và http status 200 nếu thành công, ngược lại trả về http status 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findGenreByName(@PathVariable("id") String id) {
        try {
            var genre = genreRepository.findById(id).get();
            return new ResponseEntity<>(genre, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
        }
    }

    /**
     * Tạo thể loại sách mới
     * 
     * @param genre thông tin thể loại sách
     * @return Http status 201 và thể loại sách nếu thành công, ngược lại trả về 400
     */
    @PostMapping()
    public ResponseEntity<?> createGenre(@RequestBody Genre genre) {
        try {
            Genre savedGenre = genreRepository.save(genre);
            return new ResponseEntity<>(savedGenre, HttpStatusCode.valueOf(201));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }

    /**
     * 
     * @param id id thể loại sách
     * @return Http status 200 nếu thành công, ngược lại 400 nếu thất bại
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable(name="id") String id) {
        try {
            genreRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }

    /**
     * 
     * @param id id thể loại sách
     * @param newGenre thông tin sách cập nhật
     * @return Http status 201 và thể loại sách nếu thành công, ngược lại trả về 400
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable(name = "id") String id,
            @RequestBody Genre newGenre) {
        try {
            Genre genre = genreRepository.findById(id).get();
            genre.setName(newGenre.getName());
            genre.setDescription(newGenre.getDescription());
            Genre result = genreRepository.save(genre);
            return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }
}
