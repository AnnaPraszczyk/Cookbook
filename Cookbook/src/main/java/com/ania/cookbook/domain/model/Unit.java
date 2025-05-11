package com.ania.cookbook.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter

public enum Unit {
    G("g",1f), DAG("dag",10f), KG("kg",1000f),
    OZ("oz",28f), LB("lb",454f), ST("st",6350f);

    private final String displayName;
    private final float grams;
}

