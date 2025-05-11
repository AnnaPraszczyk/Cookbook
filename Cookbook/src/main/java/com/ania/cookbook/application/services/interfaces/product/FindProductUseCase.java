package com.ania.cookbook.application.services.interfaces.product;

import com.ania.cookbook.domain.model.Product;

import java.util.Optional;

public interface FindProductUseCase {
    Optional<Product> findProductByName(FindProductByName productName);
    boolean existsProductByName(FindProductByName productName);

    record FindProductByName(String name){}


}
