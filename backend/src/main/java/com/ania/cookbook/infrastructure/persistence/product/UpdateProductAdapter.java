package com.ania.cookbook.infrastructure.persistence.product;

import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.repositories.product.UpdateProduct;
import com.ania.cookbook.infrastructure.mapper.ProductMapper;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UpdateProductAdapter implements UpdateProduct {
    private final SpringDataProductRepository jpaRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public Product updateProduct(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        ProductEntity updated = jpaRepository.save(entity);
        return productMapper.toDomain(updated);
    }
}
