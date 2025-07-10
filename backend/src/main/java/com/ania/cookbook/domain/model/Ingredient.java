package com.ania.cookbook.domain.model;

import com.ania.cookbook.domain.exceptions.IngredientValidationException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Ingredient {
    private final Product product;
    private final float amount;
    private final Unit unit;

    @JsonCreator
    public Ingredient(@JsonProperty("product") Product product,
                      @JsonProperty("amount") float amount,
                      @JsonProperty("unit") Unit unit) {
        this.product = product;
        if(product==null){throw new IngredientValidationException("Product cannot be null");}
        this.amount = amount;
        if(amount<=0){throw new IngredientValidationException("Amount must be greater than 0");}
        this.unit = unit;
        if(unit==null){throw new IngredientValidationException("Mass unit cannot be null");}
    }

    public static Ingredient newIngredient(Product product, float amount, Unit unit){
        return new Ingredient(product, amount, unit);
    }
}





