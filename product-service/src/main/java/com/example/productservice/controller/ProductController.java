package com.example.productservice.controller;


import com.example.productservice.dto.ProductDto;
import com.example.productservice.model.Product;
import com.example.productservice.service.ProductService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ModelMapper modelMapper;


    @GetMapping(value = "/findAll")
    public List<ProductDto> findAll() {
        return productService.findAll().stream().map(product -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
    }

    @GetMapping(value = "/find/{id}")
    public Optional<Product> findProductById(@PathVariable("id") Long id){
        return productService.findProductById(id);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ProductDto> findProductByCode(@PathVariable(value = "code") String code){
        Product product = productService.findProductByCode(code);
        ProductDto dto = modelMapper.map(product, ProductDto.class);
        return ResponseEntity.ok().body(dto);

    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto) {
        Product productRequest = modelMapper.map(productDto, Product.class);
        Product product = productService.createProduct(productRequest);
        ProductDto productResponse = modelMapper.map(product, ProductDto.class);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable(name = "id") Long id) {
        productService.deleteById(id);
        return new ResponseEntity<>("Product with id " + id + " has been deleted", HttpStatus.OK);
    }


}
