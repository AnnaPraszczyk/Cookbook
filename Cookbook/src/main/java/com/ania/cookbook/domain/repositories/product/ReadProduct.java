package com.ania.cookbook.domain.repositories.product;

import com.ania.cookbook.domain.model.Product;
import java.util.Optional;
import java.util.UUID;

public interface ReadProduct{

    Optional<Product> findProductById(UUID id);

    boolean existsProductById(UUID id);

    Optional<Product> findProductByName(String name);

    boolean existsProductByName(String name);
}