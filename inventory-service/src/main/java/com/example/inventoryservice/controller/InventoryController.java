package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.dto.ResponseDto;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    @Autowired
    private InventoryService service;

    @PostMapping("/add")
    public Inventory addInventory(@RequestBody Inventory inventory){
        return service.addInventory(inventory);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> code, @RequestParam List<Integer> quantity) {
        return service.isInStock(code,quantity);
    }

    @GetMapping(value = "/findAll")
    public List<Inventory> findAllInventory() {
        return service.findAllInventory();
    }

    @GetMapping(value = "/find/{id}")
    public ResponseDto findById(@PathVariable(value = "id") Long id){
        return service.findById(id);
    }

    @PutMapping(value = "/update/{id}")
    public Inventory updateInventory(@PathVariable(name = "id") Long id, @RequestBody Inventory inventory) {
        return service.updateInventory(id, inventory);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        service.deleteInventory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
