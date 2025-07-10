package com.ania.cookbook.infrastructure.persistence.product;

import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.repositories.product.ReadProduct;
import com.ania.cookbook.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReadProductAdapter implements ReadProduct {
    private final SpringDataProductRepository jpaRepository;
    private final ProductMapper productMapper;

    @Override
    public Optional<Product> findProductById(UUID id) {
        return jpaRepository.findById(id)
                .map(productMapper::toDomain);
    }

    @Override
    public boolean existsProductById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Optional<Product> findProductByName(String name) {
        return jpaRepository.findByProductName(name)
                .map(productMapper::toDomain);
    }

    @Override
    public boolean existsProductByName(String name) {
        return jpaRepository.existsByProductName(name);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(productMapper::toDomain)
                .toList();
    }

}
