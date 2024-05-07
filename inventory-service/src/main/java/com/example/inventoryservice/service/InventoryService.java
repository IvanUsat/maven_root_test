package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.dto.ProductResponse;
import com.example.inventoryservice.dto.ResponseDto;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class InventoryService {

    @Autowired
    private InventoryRepository repository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Inventory addInventory(Inventory inventory) {
        return repository.save(inventory);
    }


    public ResponseDto findById(Long id) {
        Inventory inventory = repository.findById(id).get();
        ProductResponse productResponse = webClientBuilder.build().get()
                .uri("http://product-service/products/code/" + inventory.getCode())
                .retrieve()
                .bodyToMono(ProductResponse.class)
                .block();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setInventory(inventory);
        responseDto.setProduct(productResponse);
        return responseDto;
    }


    public List<InventoryResponse> isInStock(List<String> code, List<Integer> quantity) {
        return repository.findByCodeIn(code).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .code(inventory.getCode())
                                .isInStock(inventory.getQuantity() >= quantity.get(0))
                                .quantity(inventory.getQuantity() - quantity.get(0))
                                .build()
                )
                .toList();
    }

    public List<Inventory> findAllInventory() {
        return repository.findAll();
    }


    public Inventory updateInventory(Long id, Inventory inventoryRequest) {
        Inventory inventory = repository.findById(id).orElseThrow(NoSuchElementException::new);
        inventory.setCode(inventoryRequest.getCode());
        inventory.setQuantity(inventoryRequest.getQuantity());
        return repository.save(inventory);
    }

    public void deleteInventory(Long id) {
        Inventory inventory = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Inventory with " + id + " id is not exist"));
        repository.delete(inventory);
    }
}
