package com.example.demo.controller;

import org.social.Dtos.request.CheckoutRequest;
import org.social.Dtos.response.CheckoutResponse;
import org.social.config.ResponseApi;
import org.social.handler.ResponseStatus;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.social.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<ResponseApi<CheckoutResponse>> checkout(@Valid @RequestBody CheckoutRequest request) {
        try {
            log.info("Checkout request received from: {}", request.getEmail());
            CheckoutResponse response = orderService.checkout(request);
            return new ResponseEntity<>(
                    new ResponseApi<>(ResponseStatus.CREATED, response),
                    HttpStatus.CREATED
            );
        } catch (IllegalArgumentException e) {
            log.warn("Validation error in checkout: {}", e.getMessage());
            return new ResponseEntity<>(
                    new ResponseApi<>(ResponseStatus.BAD_REQUEST, null),
                    HttpStatus.BAD_REQUEST
            );
        } catch (Exception e) {
            log.error("Checkout error", e);
            return new ResponseEntity<>(
                    new ResponseApi<>(ResponseStatus.INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseApi<Order>> getOrder(@PathVariable Integer orderId) {
        try {
            log.info("Fetching order with ID: {}", orderId);
            Order order = orderService.getOrderById(orderId);
            return new ResponseEntity<>(
                    new ResponseApi<>(ResponseStatus.SUCCESS, order),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.error("Get order error", e);
            return new ResponseEntity<>(
                    new ResponseApi<>(ResponseStatus.NOT_FOUND, null),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ResponseApi<List<Order>>> getUserOrders(@PathVariable Integer userId) {
        try {
            log.info("Fetching orders for user ID: {}", userId);
            List<Order> orders = orderService.getUserOrders(userId);
            return new ResponseEntity<>(
                    new ResponseApi<>(ResponseStatus.SUCCESS, orders),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            log.error("Get user orders error", e);
            return new ResponseEntity<>(
                    new ResponseApi<>(ResponseStatus.INTERNAL_SERVER_ERROR, null),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}