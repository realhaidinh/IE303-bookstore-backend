package com.bookstore.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

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

/**
 * Controller quản lý sách
 */
@RestController
@RequestMapping("api/book")
public class BookController {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 
     * @param id id sách
     * @return Http status 200 và thông tin sách nếu tìm thấy, ngược lại status 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> findBookById(@PathVariable("id") String id) {
        try {
            Book book = bookRepository.findById(id).get();
            return new ResponseEntity<>(book, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(404));
        }
    }

    /**
     * Tìm sách theo query string
     * 
     * @param author tên tác giả
     * @param genre  thể loại
     * @param title  tên sách
     * @param sort sắp xếp theo tăng dần ASC hoặc giảm dần DESC, mặc định là tăng dần
     * @param field trường sắp xếp, mặc định là id
     * @return danh sách sách thỏa mãn thông tin trên
     */
    @GetMapping()
    public List<Book> findBookByQuery(@RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "genre", required = false) List<String> genre,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort,
            @RequestParam(name = "by", required = false, defaultValue = "id") String field) {
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by(Sort.Direction.ASC, field);
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by(Sort.Direction.DESC, field);
        }
        if (author == null && genre == null && title == null)
            return bookRepository.findAll(sortable);
        else if (author == null && genre == null && title != null)
            return bookRepository.findByTitleLikeAllIgnoreCase(title, sortable);
        else if (author == null && genre != null && title == null)
            return bookRepository.findByGenreIn(genre, sortable);
        else if (author == null && genre != null && title != null)
            return bookRepository.findByTitleLikeAllIgnoreCaseAndGenreIn(title, genre, sortable);
        else if (author != null && genre == null && title == null)
            return bookRepository.findByAuthor(author, sortable);
        else if (author != null && genre == null && title != null)
            return bookRepository.findByTitleLikeAllIgnoreCaseAndAuthor(title, author, sortable);
        else if (author != null && genre != null && title == null)
            return bookRepository.findByAuthorAndGenreIn(author, genre, sortable);
        return bookRepository.findByTitleLikeAllIgnoreCaseAndAuthorAndGenreIn(title, author, genre, sortable);
    }

    /**
     * Tạo sách mới
     * 
     * @param bookForm thông tin sách
     * @param files    hình ảnh sách
     * @return Http status 201 và sách nếu thành công, ngược lại trả về 400
     */
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> createBook(@ModelAttribute BookForm bookForm,
            @RequestParam("images") MultipartFile[] files) {
        try {
            List<String> images = new ArrayList<>();
            for (MultipartFile file : files) {
                fileStorageService.saveFile(file);
                // Lưu địa chỉ file được public trên server
                images.add(String.format("http://127.0.0.1:8080/images/%s", file.getOriginalFilename()));
            }
            Book book = new Book(bookForm);
            book.setImages(images);
            Book result = bookRepository.save(book);
            return new ResponseEntity<Book>(result, HttpStatusCode.valueOf(201));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }

    /**
     * Xóa sách theo id
     * 
     * @param id id sách cần xóa
     * @return Http status 200 nếu thành công, ngược lại trả về 400
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") String id) {
        try {
            Book book = bookRepository.findById(id).get();
            var images = book.getImages();
            for (var image : images) {
                // Tìm tên file
                var split = image.split("/");
                fileStorageService.deleteFile(split[split.length - 1]);
            }
            bookRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }

    /**
     * Cập nhật thông tin sách theo id
     * 
     * @param id       id sách muốn cập nhật
     * @param bookForm thông tin sách
     * @param files    hình ảnh sách
     * @return Http status 200 và sách nếu thành công, ngược lại trả về 400
     */
    @PatchMapping(path = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updateBook(@PathVariable("id") String id,
            @ModelAttribute BookForm bookForm,
            @RequestParam(value = "images", required = false) MultipartFile[] files) {
        try {
            Book book = bookRepository.findById(id).get();
            if (files != null) {
                List<String> images = new ArrayList<>();
                for (MultipartFile file : files) {
                    fileStorageService.saveFile(file);
                    images.add(String.format("http://127.0.0.1:8080/images/%s", file.getOriginalFilename()));
                }
                book.setImages(images);
            }
            book.set(bookForm);
            Book result = bookRepository.save(book);
            return new ResponseEntity<Book>(result, HttpStatusCode.valueOf(201));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(400));
        }
    }
}
