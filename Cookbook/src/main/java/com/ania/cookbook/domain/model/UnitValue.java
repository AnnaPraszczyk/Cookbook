package com.ania.cookbook.domain.model;

import java.util.UUID;

public class UnitValue {
    private final UUID unitValueId;
    private final UUID productId;
    private final Unit unit;
    private final float weightValue;

    private UnitValue(UUID unitValueId, UUID productId, Unit unit, float weightValue) {
        if(unitValueId==null){throw new IllegalArgumentException("UnitValueId cannot be null");}
        this.unitValueId = unitValueId;
        if(productId==null){throw new IllegalArgumentException("ProductId cannot be null");}
        this.productId = productId;
        if(unit==null){throw new IllegalArgumentException("Unit cannot be null");}
        this.unit = unit;
        if(weightValue<0){throw new IllegalArgumentException("Weight value cannot be negative");}
        this.weightValue = weightValue;
    }

    public static UnitValue newUnitValue(UUID unitValueId, UUID productId, Unit unit, float weightValue) {
        return new UnitValue(unitValueId, productId, unit, weightValue);
    }

    public float convertToGram() {
        switch (this.unit) {
            case G -> {return this.weightValue; }
            case DAG -> {return this.weightValue * 10;}
            case KG -> {return this.weightValue * 1000;}
            case OZ -> {return this.weightValue * 28;}
            case LBS -> {return this.weightValue * 454;}
            case STONE -> {return this.weightValue * 6350;}
            case CWT -> {return this.weightValue * 100;}
            default -> throw new IllegalArgumentException("Invalid unit: " + this.unit);
        }
    }

    @Override
    public String toString() {
        return "UnitValue{" +
                "unitValueId=" + unitValueId +
                ", productId=" + productId +
                ", unit=" + unit +
                ", value=" + weightValue +
                '}';
    }

    public UUID getUnitValueId() {
        return unitValueId;
    }

    public UUID getProductId() {
        return productId;
    }
    public Unit getUnit() {

        return unit;
    }

    public float getWeightValue() {

        return weightValue;
    }


}

