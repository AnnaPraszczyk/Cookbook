package com.ania.cookbook.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter

public enum Category {
    APPETIZER("Appetizer"), SOUP("Soup"), MAIN_COURSE("Main Course"), SAUCE("Sauce"),
    SALAD("Salad"), PASTA("Pasta"), SNACK("Snack"), BEVERAGE("Beverage"),
    DESSERT("Dessert"), CAKE("Cake"), PIE("Pie"), BAKERY("Bakery");

    private final String displayName;

    @JsonValue
    public String toJson() {
        return displayName;
    }

    @JsonCreator
    public static Category fromJson(String displayName) {
        return Arrays.stream(Category.values())
                .filter(category -> category.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid category: " + displayName));
    }
}

