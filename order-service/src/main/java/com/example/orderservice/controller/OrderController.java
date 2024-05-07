package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import org.modelmapper.ModelMapper;
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

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping(value = "/add")
    public ResponseEntity<?> createOrder(@RequestBody OrderDto orderDto) {
        Order orderRequest = modelMapper.map(orderDto, Order.class);
        Order order = orderService.createOrder(orderRequest);
        OrderDto orderResponse = modelMapper.map(order, OrderDto.class);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
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
