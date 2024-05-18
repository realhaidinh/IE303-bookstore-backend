package com.bookstore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.bookstore.model.Author;

@RepositoryRestResource(collectionResourceRel = "author", path = "author")
public interface AuthorRepository extends MongoRepository<Author, String>{
    /**
     * Tìm tác giả theo tên tác giả
     * @param author tên tác giả
     * @return tác giả
     */
    Author findByName(@Param(value = "author") String author);
    Page<Author> findByNameLikeIgnoreCase(@Param(value = "author") String author, Pageable page);
}
