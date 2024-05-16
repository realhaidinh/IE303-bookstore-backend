package com.bookstore.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import com.bookstore.model.Genre;

public interface GenreRepository extends MongoRepository<Genre, String>{
    /**
     * Tìm thể loại sách theo tên thể loại
     * @param name tên thể loại sách
     * @return thể loại sách
     */
    Genre findByName(@Param(value = "name") String name);

}
