package com.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bookstore.model.Book;

@RepositoryRestResource(collectionResourceRel = "book", path = "book")
public interface BookRepository extends MongoRepository<Book, String>{
    /**
     * Tìm sách có tên giống
     * @param title tên sách
     * @param page query page
     * @return danh sách sách có tên giống
     */
    Page<Book> findByTitleLikeAllIgnoreCase(@Param(value = "title") String title, Pageable page);
    /**
     * Tìm sách theo tác giả
     * @param author tên tác giả
     * @param page query page
     * @return danh sách sách theo tác giả
     */
    Page<Book> findByAuthor(@Param(value = "author") String author, Pageable page);
    /**
     * Tìm sách theo thể loại
     * @param genre tên thể loại
     * @param page query page
     * @return danh sách sách theo thể loại
     */
    Page<Book> findByGenre(@Param(value = "genre") String genre, Pageable page);
}
