package com.ania.cookbook.domain.model;

import lombok.Getter;
@Getter

public enum Category {
    APPETIZER("Appetizer"), SOUP("Soup"), MAIN_DISH("Main Dish"), SAUCE("Sauce"),
    SALAD("Salad"), PASTA("Pasta"), SNACK("Snack"), OTHER("Other"),
    DESSERT("Dessert"), CAKE("Cake"), PIE("Pie"), BAKERY("Bakery");


    private final String displayName;

    Category(String displayName) {

        this.displayName = displayName;
    }
}
