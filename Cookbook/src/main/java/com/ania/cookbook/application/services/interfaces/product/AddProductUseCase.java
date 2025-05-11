package com.ania.cookbook.application.services.interfaces.product;

import com.ania.cookbook.domain.model.Product;

public interface AddProductUseCase {
    Product addProduct(AddProduct productName);

    record AddProduct(String name){}
}


