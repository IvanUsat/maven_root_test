package com.example.inventoryservice.dto;

import com.example.inventoryservice.model.Inventory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private Inventory inventory;
    private ProductResponse product;
}
