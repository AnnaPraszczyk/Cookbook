package com.ania.cookbook.domain.model;

import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Optional;

public class Ingredient {
    @JsonProperty("product")
    private final Optional<Product> product;
    @JsonProperty("productEntity")
    private final Optional<ProductEntity> productEntity;
    @JsonProperty("amount")
    private final float amount;
    @JsonProperty("unit")
    private final Optional<Unit> unit;
    @JsonProperty("unitValue")
    private final Optional<UnitValue> unitValue;

    private Ingredient(Product product, ProductEntity productEntity, float amount, Unit unit,UnitValue unitValue) {
        this.product = Optional.ofNullable(product);
        this.productEntity = Optional.ofNullable(productEntity);
        if(this.product.isEmpty() && this.productEntity.isEmpty()){throw new IllegalArgumentException("Product cannot be null");}
        if(this.product.isPresent() && this.productEntity.isPresent()){throw new IllegalArgumentException("Only one of product or productEntity can be present");}
        this.amount = amount;
        if(amount<=0){throw new IllegalArgumentException("Amount must be greater than 0");}
        this.unit = Optional.ofNullable(unit);
        this.unitValue = Optional.ofNullable(unitValue);
        if(this.unit.isEmpty() && this.unitValue.isEmpty()){throw new IllegalArgumentException("Unit cannot be null");}
        if(this.unit.isPresent() && this.unitValue.isPresent()){throw new IllegalArgumentException("Only one of unit or unitValue can be present");}

    }

    public Ingredient() {
        this.product = Optional.empty();
        this.productEntity = Optional.empty();
        this.amount = 0;
        this.unit = Optional.empty();
        this.unitValue = Optional.empty();
    }

    public static Ingredient newIngredient(Product product, ProductEntity productEntity, float amount, Unit unit, UnitValue unitValue ){
        return new Ingredient(product, productEntity, amount, unit, unitValue);
    }

    public Optional<Product> getProduct() {
        return product;
    }

    public Optional<ProductEntity> getProductEntity() {
        return productEntity;
    }

    public float getAmount() {
        return amount;
    }

    public Optional<Unit> getUnit() {
        return unit;
    }

    public Optional<UnitValue> getUnitValue() {
        return unitValue;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "product=" + product.map(Product::toString).orElse("Optional.empty") +
                ", productEntity=" + productEntity +
                ", amount=" + amount +
                ", unit=" + unit +
                ", unitValue=" + unitValue +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Float.compare(that.amount, amount) == 0 &&
                Objects.equals(product, that.product) &&
                Objects.equals(productEntity, that.productEntity) &&
                Objects.equals(unit, that.unit) &&
                Objects.equals(unitValue, that.unitValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, productEntity, amount, unit, unitValue);
    }
}


