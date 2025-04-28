package com.ania.cookbook.infrastructure.persistence.entity;

import com.ania.cookbook.domain.model.Unit;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "unit_value")
public class UnitValueEntity {

    @Id
    @GeneratedValue
    @Column(name="unitvalue_id", nullable = false, updatable = false)
    private final UUID unitValueId;

    @JoinColumn(name = "product_id", nullable = false)
    private final UUID productId;

    @Enumerated(EnumType.STRING)
    @Column(name="unit", nullable = false)
    private final Unit unit;

    @Column(name="weight_value", nullable = false)
    private final float weightValue;

    private UnitValueEntity(UUID unitValueId, UUID productId, Unit unit, float weightValue) {
        if(unitValueId==null){throw new IllegalArgumentException("UnitValueId cannot be null");}
        this.unitValueId = unitValueId;
        if(productId==null){throw new IllegalArgumentException("ProductId cannot be null");}
        this.productId = productId;
        if(unit==null){throw new IllegalArgumentException("Unit cannot be null");}
        this.unit = unit;
        if(weightValue<0){throw new IllegalArgumentException("Weight value cannot be negative");}
        this.weightValue = weightValue;
    }

    public UnitValueEntity() {
        this.unitValueId = UUID.randomUUID();
        this.productId = null;
        this.unit = null;
        this.weightValue = 1f;
    }

    public static UnitValueEntity newUnitValueEntity(UUID unitValueId, UUID productId, Unit unit, float weightValue) {
        return new UnitValueEntity(unitValueId, productId, unit, weightValue);
    }

    public float convertToGram() {
        switch (this.unit) {
            case G -> {
                return this.weightValue;
            }
            case DAG -> {
                return this.weightValue * 10;
            }
            case KG -> {
                return this.weightValue * 1000;
            }
            case OZ -> {
                return this.weightValue * 28;
            }
            case LBS -> {
                return this.weightValue * 454;
            }
            case STONE -> {
                return this.weightValue * 6350;
            }
            case CWT -> {
                return this.weightValue * 100;
            }
            default -> throw new IllegalArgumentException("Invalid unit: " + this.unit);
        }
    }
    @Override
    public String toString() {
        return "UnitValueEntity{" +
                "unitValueId=" + unitValueId +
                ", productId=" + productId +
                ", unit=" + unit +
                ", weightValue=" + weightValue +
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