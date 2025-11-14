package com.example.orders_services.repository;

import com.example.orders_services.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends MongoRepository<Order, UUID> {
    // Custom query methods (optional)
    List<Order> findByStatus(String status);
    List<Order> findByCustomerEmail(String email);
}
