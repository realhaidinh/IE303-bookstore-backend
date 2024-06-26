package com.bookstore.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.domain.Sort;
import com.bookstore.model.Book;

@RepositoryRestResource(collectionResourceRel = "book", path = "book")
public interface BookRepository extends MongoRepository<Book, String>{
    /**
     * Tìm sách có tên giống
     * @param title tên sách
     * @return danh sách sách có tên giống
     */
    List<Book> findByTitleLikeAllIgnoreCase(@Param(value = "title") String title, Sort sortable);
    /**
     * Tìm sách theo tác giả
     * @param author tên tác giả
     * @return danh sách sách theo tác giả
     */
    List<Book> findByAuthor(@Param(value = "author") String author, Sort sortable);
    /**
     * Tìm sách theo thể loại
     * @param genre tên thể loại
     * @return danh sách sách theo thể loại
     */
    List<Book> findByGenreIn(@Param(value = "genre") List<String> genre, Sort sortable);

    List<Book> findByAuthorAndGenreIn(@Param(value = "author") String author, @Param(value = "genre") List<String> genre, Sort sortable);

    List<Book> findByTitleLikeAllIgnoreCaseAndGenreIn(@Param(value = "title") String title, @Param(value = "genre") List<String> genre, Sort sortable);
    List<Book> findByTitleLikeAllIgnoreCaseAndAuthorAndGenreIn(@Param(value = "title") String title, @Param(value = "author") String author, @Param(value = "genre") List<String> genre, Sort sortable);
    List<Book> findByTitleLikeAllIgnoreCaseAndAuthor(@Param(value = "title") String title, @Param(value = "author") String author, Sort sortable);
}
