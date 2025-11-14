package com.example.orders_services.service;

import com.example.commonlibrary.config.CommonAutoConfiguration;
import com.example.commonlibrary.exception.BadRequestException;
import com.example.commonlibrary.exception.ResourceNotFoundException;
import com.example.commonlibrary.metrics.service.MetricsService;
import com.example.orders_services.dto.OrderRequest;
import com.example.orders_services.model.Order;
import com.example.orders_services.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final MetricsService metricsService;

    public OrderService(OrderRepository orderRepository, MetricsService metricsService) {
        this.orderRepository = orderRepository;
        this.metricsService = metricsService;
    }

    private static final Logger log = LoggerFactory.getLogger(CommonAutoConfiguration.class);

    public Order createOrder(OrderRequest orderRequest) {
        long startTime = System.currentTimeMillis();

        try {
            log.info("Creating new order for product: {}", orderRequest.getProductName());

            // Validation
            if (orderRequest.getQuantity() <= 0) {
                throw new BadRequestException("Quantity must be greater than 0");
            }
            if (orderRequest.getPrice() <= 0) {
                throw new BadRequestException("Price must be greater than 0");
            }

            // Calculate total amount
            double totalAmount = orderRequest.getPrice() * orderRequest.getQuantity();

            // Build order entity
            Order order = new Order(
                    orderRequest.getProductName(),
                    orderRequest.getQuantity(),
                    orderRequest.getPrice(),
                    totalAmount,
                    orderRequest.getCustomerEmail(),
                    "PENDING",
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            // Save to MongoDB
            Order savedOrder = orderRepository.save(order);
            log.info("Order created successfully with ID: {}", savedOrder.getId());

            // Record success metrics
            metricsService.recordOperationSuccess("create-order");
            metricsService.recordOperationDuration("create-order", startTime);

            return savedOrder;

        } catch (BadRequestException e) {
            metricsService.recordOperationFailure("create-order");
            metricsService.recordOperationDuration("create-order", startTime);
            throw e;
        } catch (Exception e) {
            log.error("Failed to create order", e);
            metricsService.recordOperationFailure("create-order");
            metricsService.recordOperationDuration("create-order", startTime);
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
    }

    public List<Order> getAllOrders() {
        long startTime = System.currentTimeMillis();
        try{

            log.info("Fetching all orders");
            List<Order> orders = orderRepository.findAll();
            metricsService.recordOperationSuccess("get-all-order");
            metricsService.recordOperationDuration("get-all-order", startTime);
            return orders;
        }catch (ResourceNotFoundException e) {
            metricsService.recordOperationFailure("get-all-order");
            metricsService.recordOperationDuration("get-all-order", startTime);
            throw e;
        }
    }

    public Order getOrderById(UUID id) {
        long startTime = System.currentTimeMillis();
        try {

            log.info("Fetching order with ID: {}", id);
            Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));

            metricsService.recordOperationSuccess("get-order");
            metricsService.recordOperationDuration("get-order", startTime);

            return order;

        } catch (ResourceNotFoundException e) {
            metricsService.recordOperationFailure("get-order");
            metricsService.recordOperationDuration("get-order", startTime);
            throw e;
        }
    }

    public List<Order> getOrdersByStatus(String status) {
        long startTime = System.currentTimeMillis();
        try {

            log.info("Fetching order with status: {}", status);

            List<Order> orders = orderRepository.findByStatus(status);

            metricsService.recordOperationSuccess("get-order-by-status");
            metricsService.recordOperationDuration("get-order-by-status", startTime);

            return orders;

        } catch (ResourceNotFoundException e) {
            metricsService.recordOperationFailure("get-order-by-status");
            metricsService.recordOperationDuration("get-order-by-status", startTime);
            throw e;
        }
    }

    public List<Order> getOrdersByEmail(String email) {
        long startTime = System.currentTimeMillis();
        try {

            log.info("Fetching order with Email: {}", email);
            List<Order> orders = orderRepository.findByCustomerEmail(email);
            metricsService.recordOperationSuccess("get-order-by-email");
            metricsService.recordOperationDuration("get-order-by-email", startTime);
            return orders;

        } catch (ResourceNotFoundException e) {
            metricsService.recordOperationFailure("get-order-by-email");
            metricsService.recordOperationDuration("get-order-by-email", startTime);
            throw e;
        }
    }

    public void deleteOrder(UUID id) {
        long startTime = System.currentTimeMillis();
        try {
            log.info("Deleting order with ID: {}", id);

            orderRepository.deleteById(id);

            metricsService.recordOperationSuccess("delete-order");
            metricsService.recordOperationDuration("delete-order", startTime);
        } catch (ResourceNotFoundException e) {
            metricsService.recordOperationFailure("delete-order");
            metricsService.recordOperationDuration("delete-order", startTime);
            throw e;
        }
    }


}
