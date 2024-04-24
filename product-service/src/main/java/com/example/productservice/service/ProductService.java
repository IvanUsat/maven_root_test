package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findProductById(id);
    }

    public Product findProductByCode(String code) {
        Optional<Product> product = productRepository.findProductByCode(code);
        if (product.isPresent()) {
            return product.get();
        } else {
            throw new ResourceNotFoundException("Product with code " + code + " has not found");
        }
    }


    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product with " + id + " id is not exist"));
        productRepository.delete(product);
    }
}
