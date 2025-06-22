package com.ania.cookbook.infrastructure.persistence.product;

import com.ania.cookbook.domain.repositories.product.DeleteProduct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class DeleteProductAdapter implements DeleteProduct {
    private final SpringDataProductRepository jpaRepository;

    @Override
    @Transactional
    public void deleteProductById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
