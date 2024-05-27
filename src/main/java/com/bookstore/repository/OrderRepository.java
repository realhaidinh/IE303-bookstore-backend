package com.bookstore.repository;
import com.bookstore.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "order", path = "order")
public interface OrderRepository extends MongoRepository<Order, String>{
    List<Order> findByUsername(@Param(value = "username") String username);
}
