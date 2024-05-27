package com.bookstore.repository;
import com.bookstore.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends MongoRepository<User, String>{
    /**
     * Tìm tài khoản theo tên tài khoản người dùng (username)
     * @param username tên tài khoản người dùng (username)
     * @return tài khoản
     */
    User findByUsername(@Param(value = "username") String username);
}
