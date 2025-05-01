package com.ania.cookbook.domain.model;

public enum MassUnit {
    G("g"), DAG("dag"), KG("kg"),
    OZ("oz"), LBS("lbs"), STONE("st"), CWT("cwt"),

    L("l"), ML("ml"),
    SPOON("sp"), TEASPOON("tsp"), GLASS("gls"), CUP("c"),
    PINT("pt"), QUART("qt"), GALLON("gal"), PIECE("pc"),
    BOTTLE("btl"), CAN("cn"),
    PACK("pack"), PACKET("packet"), JAR("jr"), CARTON("ctn");

    private final String displayName;

    Unit(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
