package com.ania.cookbook.infrastructure.mapper;

import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.ania.cookbook.infrastructure.persistence.product.SpringDataProductRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductMapper {
    private final SpringDataProductRepository jpaRepo;

    public ProductMapper(SpringDataProductRepository jpaRepo){
        this.jpaRepo = jpaRepo;
    }

    public ProductEntity toEntity(Product domain) {
        if (domain.getProductId() == null) {
            return ProductEntity.newProductEntity(UUID.randomUUID(), domain.getProductName().name());
        }
        return jpaRepo.getReferenceById(domain.getProductId());
    }

    public Product toDomain(ProductEntity entity) {
        return Product.newProduct(
                entity.getProductId(),
                new ProductName(entity.getProductName())
        );
    }

}
