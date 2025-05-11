package com.ania.cookbook.application.services.interfaces.product;

public interface DeleteProductUseCase {
    void removeProduct(DeleteProductName productName);

    record DeleteProductName(String name){}
}
