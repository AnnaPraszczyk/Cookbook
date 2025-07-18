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
    OZ("oz",28f), LB("lb",454f), ST("st",6350f),
    ML("ml",1f),CL("cl",10f), DL("dl",100f), L("l",1000f),
    TSP("tsp",5f), TBSP("tbsp",15f), CUP("cup", 250f),
    PT("pt",473f), QT("qt",946f), GAL("gal",3785f),
    PC("pc",150f), SL("sl",25f), PN("pn",1f);

    private final String displayName;
    private final float grams;

    @JsonCreator
    public static Unit fromString(String s) {
        String normalized = s.trim().toLowerCase();
        return Arrays.stream(values())
                .filter(u -> u.displayName.equals(normalized) || u.name().toLowerCase().equals(normalized))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown unit: " + s));
    }

    @JsonValue
    public String toValue() {
        return displayName;
    }

    public float toGrams(float amount) {
        return amount * grams;
    }
}

