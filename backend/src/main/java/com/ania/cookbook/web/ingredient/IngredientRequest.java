package com.ania.cookbook.web.ingredient;

import com.ania.cookbook.domain.model.Unit;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record IngredientRequest(@NotBlank @JsonProperty("productName") String productName,
                                @PositiveOrZero @JsonProperty("amount") float amount,
                                @NotNull @JsonProperty("unit") Unit unit)  {}
