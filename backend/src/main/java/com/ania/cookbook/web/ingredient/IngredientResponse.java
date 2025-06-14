package com.ania.cookbook.web.ingredient;

import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.model.Unit;
import java.util.UUID;

public record IngredientResponse(UUID productId, ProductName productName, float amount, Unit unit) {}
