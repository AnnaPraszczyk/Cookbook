package com.ania.cookbook.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter

public enum Category {
    APPETIZER("Appetizer"), SOUP("Soup"), MAIN_COURSE("Main Course"), SAUCE("Sauce"),
    SALAD("Salad"), PASTA("Pasta"), SNACK("Snack"), BEVERAGE("Beverage"),
    DESSERT("Dessert"), CAKE("Cake"), PIE("Pie"), BAKERY("Bakery");

    private final String displayName;
}

