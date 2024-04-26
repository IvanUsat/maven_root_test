package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WebClient.Builder webClientBuilder;


    public Order createOrder(Order order) {
        Order newOrder = new Order();
        newOrder.setNumber(order.getNumber());
        newOrder.setItems(order.getItems());

        List<String> codes = newOrder.getItems().stream()
                .map(OrderItem::getCode)
                .toList();
        List<Integer> quantity = newOrder.getItems().stream()
                .map(OrderItem::getQuantity)
                .toList();

        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri("http://inventory-service/inventory",
                        uriBuilder -> uriBuilder
                                .queryParam("code", codes)
                                .queryParam("quantity", quantity)
                                .build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean result = Arrays.stream(inventoryResponses)
                .allMatch(InventoryResponse::isInStock);

        if (result) {
            return orderRepository.save(order);
        } else throw new IllegalArgumentException("Product is not in stock, please try again later");
    }

    public Order updateOrder(String number, Order orderRequest) {
        Order order = orderRepository.findOrderByNumber(number);
        order.setOrderStatus(orderRequest.getOrderStatus());
        List<OrderItem> itemList = new ArrayList<>();
        for (OrderItem it : orderRequest.getItems()) {
            OrderItem items = new OrderItem(it.getCode(), it.getQuantity(), it.getPrice());
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
