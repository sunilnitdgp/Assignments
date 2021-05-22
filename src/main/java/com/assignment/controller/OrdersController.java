package com.assignment.controller;

import com.assignment.model.Order;
import com.assignment.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrdersController {

    @Autowired private OrdersRepository ordersRepository;

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody List<Order> order) {
        Long orderId = ordersRepository.placeOrder(order);
        return ResponseEntity.ok(orderId);
    }
}
