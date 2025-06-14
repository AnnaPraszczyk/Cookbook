package com.ania.cookbook.web.product;

import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;

public record ProductRequest(ProductName productName) {}
