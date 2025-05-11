package com.ania.cookbook.application.services.interfaces.product;

import com.ania.cookbook.domain.model.Product;

public interface UpdateProductUseCase {
    Product updateProductName(UpdateProductName updateProduct);

    record UpdateProductName(String name, String newName){}
}
