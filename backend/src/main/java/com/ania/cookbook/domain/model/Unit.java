package com.ania.cookbook.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum Unit {
    G("g",1f), DAG("dag",10f), KG("kg",1000f),
    OZ("oz",28f), LB("lb",454f), ST("st",6350f);

    private final String displayName;
    private final float grams;

    @JsonCreator
    public static Unit fromString(String s) {
        // ignorujemy wielkość liter i dopasowujemy do displayName lub nazwy enuma
        String normalized = s.trim().toLowerCase();
        return Arrays.stream(values())
                .filter(u -> u.displayName.equals(normalized) || u.name().toLowerCase().equals(normalized))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown unit: " + s));
    }

    @JsonValue
    public String toValue() {
        return name();      // lub displayName, jeśli chcesz zwracać np. "g" zamiast "G"
    }

    public float toGrams(float amount) {
        return amount * grams;
    }
}

