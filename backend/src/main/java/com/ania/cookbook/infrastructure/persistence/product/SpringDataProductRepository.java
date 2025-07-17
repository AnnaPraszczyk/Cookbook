package com.ania.cookbook.infrastructure.persistence.product;

import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, UUID> {
    Optional<ProductEntity> findByProductName(String name);
    boolean existsByProductName(String name);
}
