package com.bookstore.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
    public Page<Genre> findAllGenre(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "size", required = false, defaultValue = "5") Integer pageSize) {
        return genreRepository.findAll(PageRequest.of(pageNumber, Integer.min(pageSize, 20)));
    }

    /**
     * Tìm thể loại sách theo tên thể loại
     * 
     * @param name tên thể loại sách
     * @return thể loại sách
     */
    @GetMapping("/{name}")
    public Genre findGenreByName(@PathVariable("name") String name) {
        return genreRepository.findByName(name);
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
    @DeleteMapping()
    public ResponseEntity<?> deleteGenre(@RequestParam(name = "id", required = true) String id) {
        try {
            genreRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }

    /**
     * 
     * @param id       id thể loại sách
     * @param newGenre thông tin sách cập nhật
     * @return Http status 201 và thể loại sách nếu thành công, ngược lại trả về 400
     */
    @PatchMapping()
    public ResponseEntity<?> updateGenre(@RequestParam(name = "id", required = true) String id,
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
