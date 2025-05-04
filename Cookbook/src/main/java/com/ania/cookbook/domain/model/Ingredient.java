package com.ania.cookbook.domain.model;

import com.ania.cookbook.domain.exceptions.IngredientValidationException;
import lombok.Getter;

@Getter

public class Ingredient {
    private final Product product;
    private final float amount;
    private VolumeUnit volumeUnit;
    private float massValue;
    private final MassUnit massUnit;


    public Ingredient() {
        this.product = null;
        this.amount = 0;
        this.volumeUnit = null;
        this.massValue = 0;
        this.massUnit = null;
    }

    private Ingredient(Product product, float amount, MassUnit massUnit) {
        this.product = product;
        if(product==null){throw new IngredientValidationException("Product cannot be null");}
        this.amount = amount;
        if(amount<=0){throw new IngredientValidationException("Amount must be greater than 0");}
        this.massUnit = massUnit;
        if(massUnit==null){throw new IngredientValidationException("Mass unit cannot be null");}
    }

    private Ingredient(Product product, float amount, VolumeUnit volumeUnit, float massValue, MassUnit massUnit) {
        this.product = product;
        if(product==null){throw new IngredientValidationException("Product cannot be null");}
        this.amount = amount;
        if(amount<=0){throw new IngredientValidationException("Amount must be greater than 0");}
        this.volumeUnit = volumeUnit;
        if(volumeUnit==null){throw new IngredientValidationException("Volume unit cannot be null");}
        this.massValue = massValue;
        if(massValue<=0){throw new IngredientValidationException("Mass value must be greater than 0");}
        this.massUnit = massUnit;
        if(massUnit==null){throw new IngredientValidationException("Mass unit cannot be null");}
    }

    public static Ingredient newIngredient(Product product, float amount, MassUnit unit){
        return new Ingredient(product, amount, unit);
    }
    public static Ingredient newIngredient(Product product, float amount, VolumeUnit volumeUnit, float massValue, MassUnit unit){
        return new Ingredient(product, amount, volumeUnit,massValue,unit);
    }
}


