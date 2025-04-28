package com.ania.cookbook.domain.repositories.product;

import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;

import java.util.Optional;
import java.util.UUID;

public interface ReadProduct {
    Optional<ProductEntity> findProductById(UUID id);

    boolean existsProductById(UUID id);

    ProductEntity findProductByName(String name);

    boolean existsProductByName(String name);
}