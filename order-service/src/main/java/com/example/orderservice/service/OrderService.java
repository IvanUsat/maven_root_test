package com.example.orderservice.service;

import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;


    public Order createOrder(Order order) {
        Order newOrder = new Order();
        newOrder.setNumber(order.getNumber());
        newOrder.setItems(order.getItems());
        return orderRepository.save(order);
    }

    public Order updateOrder(String number, Order orderRequest) {
        Order order = orderRepository.findOrderByNumber(number);
        order.setOrderStatus(orderRequest.getOrderStatus());
        List<OrderItem> itemList = new ArrayList<>();
        for (OrderItem it : orderRequest.getItems()) {
            OrderItem items = new OrderItem(it.getQuantity(), it.getPrice());
            items.setOrder(order);
            itemList.add(items);
        }
        order.setItems(itemList);
        return orderRepository.save(order);
    }



    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    public Order findOrderById(Long id) {
        Optional<Order> result = Optional.ofNullable(orderRepository.findOrderById(id));
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new ResourceNotFoundException("Order with id " + id + " has not found");
        }
    }




    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

}
