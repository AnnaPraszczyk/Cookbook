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

    public float toGrams(float amount) {
        return amount * grams;
    }

    @JsonValue
    public String toJson() {
        return displayName;
    }

    @JsonCreator
    public static Unit fromJson(String displayName) {
        return Arrays.stream(Unit.values())
                .filter(unit -> unit.getDisplayName().equalsIgnoreCase(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid unit: " + displayName));
    }
}

