package com.ania.cookbook.application.services.implementations.list;
import static io.micrometer.common.util.StringUtils.isBlank;

public record ListName(String name) {
    public ListName {
        if (isBlank(name)) {
            throw new IllegalArgumentException("List name cannot be null or blank");
        }
    }
}
