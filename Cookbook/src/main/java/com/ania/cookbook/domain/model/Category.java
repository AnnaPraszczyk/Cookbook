package com.ania.cookbook.domain.model;

public enum Category {
    APPETIZER("Appetizer"), SOUP("Soup"), MAIN_DISH("Main Dish"), SAUCE("Sauce"),
    SALAD("Salad"), PASTA("Pasta"), SNACK("Snack"), OTHER("Other"),
    DESSERT("Dessert"), CAKE("Cake"), PIE("Pie"), BAKERY("Bakery"),
    VEGETARIAN("Vegetarian"), VEGAN("Vegan"), GLUTEN_FREE("Gluten-free"), KETO("Keto"),
    POLISH("Polish"), AMERICAN("American"), ASIAN("Asian"), FRENCH("French"),
    ITALIAN("Italian"), MEXICAN("Mexican");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
