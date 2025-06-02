package com.ania.cookbook.web.ingredient;

import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.model.Unit;
import com.fasterxml.jackson.annotation.JsonProperty;

public record IngredientRequest(@JsonProperty("productName") ProductName productName,
                                @JsonProperty("amount") float amount,
                                @JsonProperty("unit") Unit unit)  {}
