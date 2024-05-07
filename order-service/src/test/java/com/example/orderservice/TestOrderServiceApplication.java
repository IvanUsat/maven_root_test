package com.example.orderservice;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.model.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TestOrderServiceApplication {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderService service;

    @Mock
    private WebClient.Builder webClientBuilder;


    private Order order;


    @BeforeEach
    public void setup(){
        order = Order.builder()
                .number("1001")
                .build();
    }
    @Test
    public void testCreateOrder(){
        OrderItem item1 = new OrderItem("ABC123", 2, 500.0);
        OrderItem item2 = new OrderItem("DEF456", 1 , 800.0);
        List<OrderItem> items = Arrays.asList(item1, item2);
        Order order = new Order(1L, "12345",OrderStatus.AT_WORK, items);

        InventoryResponse response1 = new InventoryResponse("ABC123", true);
        InventoryResponse response2 = new InventoryResponse("DEF456", false);
        InventoryResponse[] inventoryResponses = {response1, response2};

        when(webClientBuilder.build().get().uri(anyString()).retrieve().bodyToMono(InventoryResponse[].class).block()).thenReturn(inventoryResponses);
        when(repository.save(order)).thenReturn(order);

        Order result = service.createOrder(order);

        Assertions.assertThat(result).isNotNull();
        org.junit.jupiter.api.Assertions.assertEquals(order, result);
    }


    @Test
    public void testFindAllOrders(){
        Order order2 = Order.builder()
                .number("1002")
                .build();
        List<Order> list = Arrays.asList(order, order2);
        when(repository.findAll()).thenReturn(list);
        List<Order> result = service.findAllOrders();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);
    }


    @Test
    public void testFindOrderById(){
        long orderId = 1;
        when(repository.findById(orderId)).thenReturn(Optional.ofNullable(order));
        Order result = service.findOrderById(orderId);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void testDeleteOrder(){
        long id = 1;
        when(repository.findById(id)).thenReturn(Optional.ofNullable(order));
        doNothing().when(repository).delete(order);
        assertAll(()-> service.deleteOrder(id));
    }


    @Test
    public void testUpdateOrder() {
        String number = "123";
        Order order = new Order();
        order.setOrderStatus(OrderStatus.COMPLETE);
        OrderItem item = new OrderItem("iphone_123", 1, 100.0);
        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        Order orderRequest = new Order();
        orderRequest.setOrderStatus(OrderStatus.COMPLETE);
        orderRequest.setItems(items);

        when(repository.findOrderByNumber(number)).thenReturn(order);
        when(repository.save(order)).thenReturn(order);

        Order updatedOrder = service.updateOrder(number, orderRequest);

        Assertions.assertThat(updatedOrder).isNotNull();
    }

}

