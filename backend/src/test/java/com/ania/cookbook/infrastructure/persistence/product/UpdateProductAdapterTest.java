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
class UpdateProductAdapterTest {
    @Mock
    SpringDataProductRepository jpaRepository;

    @Mock
    ProductMapper productMapper;

    @InjectMocks
    UpdateProductAdapter adapter;

    @Test
    void updateProductAndReturnMappedVersion() {
        UUID id = UUID.randomUUID();
        Product original = Product.newProduct(id, new ProductName("Flour"));
        ProductEntity entity = ProductEntity.newProductEntity(id, "Flour");
        ProductEntity updatedEntity = ProductEntity.newProductEntity(id, "Flour");
        Product updated = Product.newProduct(id, new ProductName("Flour"));

        when(productMapper.toEntity(original)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(updatedEntity);
        when(productMapper.toDomain(updatedEntity)).thenReturn(updated);

        Product result = adapter.updateProduct(original);

        assertEquals("Flour", result.getProductName().name());
        assertEquals(id, result.getProductId());
        verify(jpaRepository).save(entity);
        verify(productMapper).toEntity(original);
        verify(productMapper).toDomain(updatedEntity);
    }

    @Test
    void throwValidationExceptionWhenProductNameIsNull() {
        UUID id = UUID.randomUUID();
        assertThrows(ProductValidationException.class, () -> Product.newProduct(id, null));
    }

    @Test
    void throwIfMapperFails() {
        UUID id = UUID.randomUUID();
        Product validProduct = Product.newProduct(id, new ProductName("Broken"));

        when(productMapper.toEntity(validProduct)).thenThrow(new IllegalStateException("Mapping failed"));

        assertThrows(IllegalStateException.class, () -> adapter.updateProduct(validProduct));
    }
}