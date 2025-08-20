package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {
    private ProductMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductMapper();
    }

    @Test
    void toEntity() {
        UUID id = UUID.randomUUID();
        Product domain = Product.newProduct(id, new ProductName("Sugar"));
        ProductEntity entity = mapper.toEntity(domain);

        assertEquals(id, entity.getProductId());
        assertEquals("Sugar", entity.getProductName());
    }

    @Test
    void toDomain() {
        UUID id = UUID.randomUUID();
        ProductEntity entity = ProductEntity.newProductEntity(id, "Milk");
        Product domain = mapper.toDomain(entity);

        assertEquals(id, domain.getProductId());
        assertEquals("Milk", domain.getProductName().name());
    }

    @Test
    void toDomainShouldThrowWhenEntityNameIsNull() {
        assertThrows(NullPointerException.class, () -> mapper.toDomain(ProductEntity.newProductEntity(UUID.randomUUID(), null)));
    }

    @Test
    void toEntityWithNullProductName() {
        UUID id = UUID.randomUUID();
        Product broken = Product.newProduct(id, null);

        assertThrows(NullPointerException.class, () ->
                mapper.toEntity(broken)
        );
    }
}