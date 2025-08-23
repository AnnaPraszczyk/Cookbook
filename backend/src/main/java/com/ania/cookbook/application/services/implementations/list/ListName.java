package com.ania.cookbook.application.services.implementations.list;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import static io.micrometer.common.util.StringUtils.isBlank;

public record ListName(String name) {
    public ListName {
        if (isBlank(name)) {
            throw new ListValidationException("List name cannot be null or blank");
        }
    }
}
