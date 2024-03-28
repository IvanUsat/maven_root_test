package com.example.productservice.dto;

import lombok.*;

@NoArgsConstructor
@Data
public class ProductDto {
    private String name;
    private Long price;
    private Boolean isAvailable;
}
