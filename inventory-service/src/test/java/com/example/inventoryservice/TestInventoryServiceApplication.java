package com.example.inventoryservice;


import com.example.inventoryservice.config.WebClientConfig;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.dto.ProductResponse;
import com.example.inventoryservice.dto.ResponseDto;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import com.example.inventoryservice.service.InventoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertAll;



@ExtendWith(MockitoExtension.class)
public class TestInventoryServiceApplication {

    @Mock
    private InventoryRepository repository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private InventoryService service;

    private Inventory inventory;

    @BeforeEach
    public void setup(){
        inventory = Inventory.builder()
                .code("iphone_15")
                .quantity(50).build();
    }
    @Test
    public void testAddInventory(){
        when(repository.save(Mockito.any(Inventory.class))).thenReturn(inventory);
        Inventory savedInventory = service.addInventory(inventory);
        Assertions.assertThat(savedInventory).isNotNull();
    }

    @Test
    public void testIsInStock(){
        List<String> code = Arrays.asList("2", "5");
        List<Integer> quantities = Arrays.asList(1, 2);
        List<Inventory> inventories = Arrays.asList(
                new Inventory(1L,"ABC", 10),
                new Inventory(1L, "DEF", 5)
        );
        when(repository.findByCodeIn(code)).thenReturn(inventories);
        List<InventoryResponse> responses = service.isInStock(code, quantities);
        org.junit.jupiter.api.Assertions.assertEquals(2, responses.size());
        org.junit.jupiter.api.Assertions.assertTrue(responses.get(0).isInStock());

    }

    @Test
    public void testFindAllInventory(){
        Inventory inventory2 = Inventory.builder()
                .code("iphone_13")
                .quantity(20)
                .build();

        List<Inventory> list = Arrays.asList(inventory, inventory2);
        when(repository.findAll()).thenReturn(list);
        List<Inventory> result = service.findAllInventory();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void testFindInventoryById(){
        long inventoryId = 1;
        ProductResponse productResponse = new ProductResponse("ABC", 20L);
        when(repository.findById(inventoryId)).thenReturn(Optional.ofNullable(inventory));
        when(webClientBuilder.build().get().uri("http://product-service/products/code/" + inventory.getCode()).retrieve().bodyToMono(ProductResponse.class).block()).thenReturn(productResponse);
        ResponseDto responseDto = service.findById(inventoryId);
        Assertions.assertThat(responseDto).isNotNull();
        org.junit.jupiter.api.Assertions.assertEquals(inventory, responseDto.getInventory());
        org.junit.jupiter.api.Assertions.assertEquals(productResponse, responseDto.getProduct());
    }

    @Test
    public void testDeleteInventoryById(){
        long inventoryId = 1;
        when(repository.findById(inventoryId)).thenReturn(Optional.ofNullable(inventory));
        doNothing().when(repository).delete(inventory);
        assertAll(()-> service.deleteInventory(inventoryId));
    }

    @Test
    public void testUpdateInventory(){
        long inventoryId = 1;
        Inventory inventoryRequest = Inventory.builder()
                .code("example")
                .quantity(2)
                .build();
        when(repository.findById(inventoryId)).thenReturn(Optional.ofNullable(inventory));
        when(repository.save(inventory)).thenReturn(inventory);
        Inventory updateReturn = service.updateInventory(inventoryId, inventoryRequest);
        Assertions.assertThat(updateReturn).isNotNull();
    }


}
