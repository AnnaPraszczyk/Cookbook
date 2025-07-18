package com.ania.cookbook.infrastructure.persistence.product;

import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.mapper.ProductMapper;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadProductAdapterTest {
    @Mock
    SpringDataProductRepository jpaRepository;

    @Mock
    ProductMapper productMapper;

    @InjectMocks
    ReadProductAdapter adapter;

    @Test
    void shouldReturnProductById() {
        UUID id = UUID.randomUUID();
        ProductEntity entity = ProductEntity.newProductEntity(id, "Sugar");
        Product domain = Product.newProduct(id, new ProductName("Sugar"));
        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(productMapper.toDomain(entity)).thenReturn(domain);
        Optional<Product> result = adapter.findProductById(id);

        assertTrue(result.isPresent());
        assertEquals("Sugar", result.get().getProductName().name());
    }

    @Test
    void shouldReturnTrueIfProductExistsById() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.existsById(id)).thenReturn(true);

        assertTrue(adapter.existsProductById(id));
    }

    @Test
    void shouldReturnProductByName() {
        ProductEntity entity = ProductEntity.newProductEntity(UUID.randomUUID(), "Milk");
        Product domain = Product.newProduct(entity.getProductId(), new ProductName("Milk"));
        when(jpaRepository.findByProductName("Milk")).thenReturn(Optional.of(entity));
        when(productMapper.toDomain(entity)).thenReturn(domain);
        Optional<Product> result = adapter.findProductByName("Milk");

        assertTrue(result.isPresent());
        assertEquals("Milk", result.get().getProductName().name());
    }

    @Test
    void shouldReturnAllProducts() {
        ProductEntity e1 = ProductEntity.newProductEntity(UUID.randomUUID(), "Eggs");
        ProductEntity e2 = ProductEntity.newProductEntity(UUID.randomUUID(), "Butter");
        Product p1 = Product.newProduct(e1.getProductId(), new ProductName("Eggs"));
        Product p2 = Product.newProduct(e2.getProductId(), new ProductName("Butter"));
        when(jpaRepository.findAll()).thenReturn(List.of(e1, e2));
        when(productMapper.toDomain(e1)).thenReturn(p1);
        when(productMapper.toDomain(e2)).thenReturn(p2);
        List<Product> result = adapter.findAll();

        assertEquals(2, result.size());
        assertEquals("Eggs", result.get(0).getProductName().name());
        assertEquals("Butter", result.get(1).getProductName().name());
    }
}