package com.ania.cookbook.domain.repositories.product;

import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;

public interface SaveProduct {
    ProductEntity saveProduct(ProductEntity product);
}
