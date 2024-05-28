package com.bookstore.repository;
import com.bookstore.model.Order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "order", path = "order")
public interface OrderRepository extends MongoRepository<Order, String>{
    /**
     * Tìm hóa đơn theo tên tài khoản người dùng
     * @param username tên tài khoản người dùng
     * @return danh sách hóa đơn
     */
    Page<Order> findByUsername(@Param(value = "username") String username, Pageable page);
}
