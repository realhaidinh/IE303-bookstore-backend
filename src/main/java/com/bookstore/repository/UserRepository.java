package com.bookstore.repository;
import com.bookstore.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends MongoRepository<User, String>{
    // User findByUsername(String username);
    User findByUsername(@Param(value = "username") String username);
}
