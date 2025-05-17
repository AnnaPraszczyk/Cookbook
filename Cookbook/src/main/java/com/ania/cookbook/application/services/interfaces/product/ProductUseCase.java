package com.ania.cookbook.application.services.interfaces.product;

import com.ania.cookbook.domain.model.Product;

import java.util.Optional;

public interface ProductUseCase {
    Product addProduct(ProductName productName);
    Optional<Product> findProductByName(ProductName productName);
    boolean existsProductByName(ProductName productName);
    Product updateProductName(ProductName productName, ProductName newName);
    void removeProduct(ProductName productName);

    record ProductName(String name){}
}


