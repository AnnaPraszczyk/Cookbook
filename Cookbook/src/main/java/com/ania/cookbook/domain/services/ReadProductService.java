package com.ania.cookbook.domain.services;

import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;
@Service
@Transactional(readOnly = true)
public class ReadProductService {
    private final InMemoryProductRepository productRepository;
    @Autowired
    public ReadProductService(InMemoryProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<ProductEntity> findProductById(UUID id) {
        return productRepository.findProductById(id);
    }

    public Optional<ProductEntity> findProductByName(String name) {
        ProductEntity product = productRepository.findProductByName(name);
        return Optional.ofNullable(product);
    }
}
