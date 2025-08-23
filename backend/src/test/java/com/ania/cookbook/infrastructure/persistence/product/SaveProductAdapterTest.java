package com.ania.cookbook.infrastructure.persistence.product;

import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.mapper.ProductMapper;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveProductAdapterTest {
    @Mock
    SpringDataProductRepository jpaRepository;
    @Mock
    ProductMapper productMapper;
    @InjectMocks
    SaveProductAdapter adapter;

    @Test
    void saveAndReturnMappedProduct() {
        UUID id = UUID.randomUUID();
        Product domainProduct = Product.newProduct(id, new ProductName("Butter"));
        ProductEntity entity = ProductEntity.newProductEntity(id, "Butter");
        ProductEntity savedEntity = ProductEntity.newProductEntity(id, "Butter");
        Product mappedBack = Product.newProduct(id, new ProductName("Butter"));
        when(productMapper.toEntity(domainProduct)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(savedEntity);
        when(productMapper.toDomain(savedEntity)).thenReturn(mappedBack);
        Product result = adapter.saveProduct(domainProduct);

        assertEquals("Butter", result.getProductName().name());
        assertEquals(id, result.getProductId());
        verify(jpaRepository).save(entity);
        verify(productMapper).toEntity(domainProduct);
        verify(productMapper).toDomain(savedEntity);
    }

    @Test
    void shouldThrowValidationExceptionWhenProductNameIsNull() {
        UUID id = UUID.randomUUID();
        assertThrows(ProductValidationException.class, () -> Product.newProduct(id, null));
    }

    @Test
    void shouldThrowExceptionIfMapperFails() {
        UUID id = UUID.randomUUID();
        Product validProduct = Product.newProduct(id, new ProductName("Broken"));
        when(productMapper.toEntity(validProduct)).thenThrow(new IllegalStateException("Mapping failed"));

        assertThrows(IllegalStateException.class, () -> adapter.saveProduct(validProduct));
    }
}