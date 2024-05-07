package com.example.productservice;

import com.example.productservice.model.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertAll;



@ExtendWith(MockitoExtension.class)
public class TestProductServiceApplication {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    private Product product;

    @BeforeEach
    public void setup(){
        product = Product.builder()
                .code("iphone_15")
                .price(500L).build();
    }
    @Test
    public void testCreateProduct(){
        when(repository.save(Mockito.any(Product.class))).thenReturn(product);
        Product savedProduct = service.createProduct(product);
        Assertions.assertThat(savedProduct).isNotNull();
    }

    @Test
    public void testFindAllProduct(){
        Product product2 = Product.builder()
                .code("iphone_13")
                .price(300L)
                .build();

        List<Product> list = Arrays.asList(product, product2);
        when(repository.findAll()).thenReturn(list);
        List<Product> result = service.findAll();
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void testFindProductById(){
        long productId = 1;
        when(repository.findById(productId)).thenReturn(Optional.ofNullable(product));
        Optional<Product> result = service.findProductById(productId);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void testDeleteProductById(){
        long productId = 1;
        when(repository.findById(productId)).thenReturn(Optional.ofNullable(product));
        doNothing().when(repository).delete(product);
        assertAll(()-> service.deleteById(productId));
    }

    @Test
    public void testFindProductByCode(){
        String productCode = "iphone_15";
        when(repository.findProductByCode(productCode)).thenReturn(Optional.ofNullable(product));
        Product result = service.findProductByCode(productCode);
        Assertions.assertThat(result).isNotNull();
    }

}
