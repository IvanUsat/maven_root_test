package com.example.orderservice.dto;


import com.example.orderservice.model.OrderItem;
import com.example.orderservice.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String number;
    private OrderStatus orderStatus = OrderStatus.AT_WORK;
    private List<OrderItem> items;
}
