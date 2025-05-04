package com.ania.cookbook.domain.model;

import lombok.Getter;

@Getter
public enum VolumeUnit {
    L("l"), ML("ml"),
    SPOON("sp"), TEASPOON("tsp"), GLASS("gls"), CUP("c"),
    PT("pt"), QT("qt"), GAL("gal"), PIECE("pc"),
    BOTTLE("btl"), CAN("cn"),OTHER("other"),
    PACK("pack"), PACKET("packet"), JAR("jr"), CARTON("ctn");

    private final String displayName;

    VolumeUnit(String displayName) {

        this.displayName = displayName;
    }
}
