package com.ania.cookbook.web.product;

import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;

import java.util.UUID;

public record ProductResponse(UUID productId, ProductName productName) {}
