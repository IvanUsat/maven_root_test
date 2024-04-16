package com.example.orderservice.controller;

import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping(value = "/add")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order orderCreated = orderService.createOrder(order);
        return new ResponseEntity<>(orderCreated, HttpStatus.CREATED);
    }

    @GetMapping(value = "/findAll")
    public List<Order> findAllOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable(name = "id") Long id) {
        Order order = orderService.findOrderById(id);
        return ResponseEntity.ok().body(order);
    }

    @PutMapping(value = "/update/{number}")
    public Order updateOrder(@PathVariable(name = "number") String number, @RequestBody Order order) {
        return orderService.updateOrder(number, order);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
