package com.genAi.springsecurityjwt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.genAi.springsecurityjwt.model.Product;
import com.genAi.springsecurityjwt.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void getAllProducts_shouldReturnProductsFromRepository() {
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = List.of(product1, product2);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> actualProducts = productService.getAllProducts();

        assertEquals(products, actualProducts);
    }

    @Test
    void getProductById_shouldReturnOptionalProduct() {
        Product product = new Product();
        product.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> actualProduct = productService.getProductById(1L);

        assertTrue(actualProduct.isPresent());
        assertEquals(1L, actualProduct.get().getId());
    }

    @Test
    void saveProduct_shouldDelegateToRepository() {
        Product product = new Product();
        product.setName("Laptop");
        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.saveProduct(product);

        assertEquals(product, savedProduct);
        verify(productRepository).save(product);
    }

    @Test
    void deleteProduct_shouldDeleteById() {
        productService.deleteProduct(7L);

        verify(productRepository).deleteById(7L);
    }
}
