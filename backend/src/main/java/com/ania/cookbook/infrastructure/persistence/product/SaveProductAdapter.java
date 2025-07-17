package com.ania.cookbook.infrastructure.persistence.product;

import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.repositories.product.SaveProduct;
import com.ania.cookbook.infrastructure.mapper.ProductMapper;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class SaveProductAdapter implements SaveProduct {
    private final SpringDataProductRepository jpaRepository;
    private final ProductMapper productMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public Product saveProduct(Product product) {
        ProductEntity entity = productMapper.toEntity(product);
        ProductEntity saved = jpaRepository.save(entity);
        return productMapper.toDomain(saved);
    }
}
