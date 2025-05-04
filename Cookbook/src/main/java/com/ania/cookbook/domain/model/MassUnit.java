package com.ania.cookbook.domain.model;

import lombok.Getter;

@Getter

public enum MassUnit {
    G("g"), DAG("dag"), KG("kg"),
    OZ("oz"), LB("lb"), ST("st");

    private final String displayName;

    MassUnit(String displayName) {

        this.displayName = displayName;
    }
/*
    public float convertToGram(MassUnit unit) {
        switch (this.unit) {
            case G -> {return this.unit; }
            case DAG -> {return this.unit * 10;}
            case KG -> {return this.unit* 1000;}
            case OZ -> {return this.unit * 28;}
            case LB -> {return this.unit * 454;}
            case STONE -> {return this.unit* 6350;}
            default -> throw new IllegalArgumentException("Invalid unit: " + this.unit);
        }
    }*/

    }
