package com.ania.cookbook.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter

public enum Category {
    APPETIZER("Appetizer"), SOUP("Soup"), MAIN_COURSE("Main Course"), SAUCE("Sauce"),
    SALAD("Salad"), PASTA("Pasta"), SNACK("Snack"), BEVERAGE("Beverage"),
    DESSERT("Dessert"), CAKE("Cake"), PIE("Pie"), BAKERY("Bakery");

    private final String displayName;

    @JsonValue
    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Category fromDisplayName(String value) {
        for (Category c : values()) {
            if (c.displayName.equalsIgnoreCase(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown category: " + value);
    }

}

