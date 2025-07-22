package com.ania.cookbook.application.services.interfaces.product;

import com.ania.cookbook.application.services.implementations.product.ProductName;
import com.ania.cookbook.domain.model.Product;
import java.util.List;
import java.util.Optional;

public interface ProductUseCase {
    Product addProduct(ProductName productName);
    Optional<Product> findProductByName(ProductName productName);
    boolean existsProductByName(ProductName productName);
    List<Product> findAll();
    Product updateProductName(ProductName productName, ProductName newName);
    void removeProduct(ProductName productName) ;
}


