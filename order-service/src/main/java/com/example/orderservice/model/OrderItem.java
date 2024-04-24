package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private Integer quantity;

    private Double price;

    @ManyToOne
    @Transient
    private Order order;

    public OrderItem(String code, Integer quantity, Double price) {
        this.setCode(code);
        this.setQuantity(quantity);
        this.setPrice(price);
    }
}
