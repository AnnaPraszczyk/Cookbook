package com.ania.cookbook.domain.model;
import com.ania.cookbook.application.services.interfaces.recipe.ListManagementUseCase.ListName;
import com.ania.cookbook.domain.exceptions.ListValidationException;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import java.time.Instant;
import java.util.List;

@Getter
public class SavedRecipeListDomain {
    private final ListName listName;
    private final Instant createdAt;
    private String listDescription;
    private final int expectedPortions;
    private final List<RecipeListEntryDomain> entries;

    @Builder
    public SavedRecipeListDomain(ListName listName, Instant createdAt, String listDescription, int expectedPortions, @Singular List<RecipeListEntryDomain> entries) {
        this.listName = listName;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.listDescription = listDescription;
        if (expectedPortions <= 0) {
            throw new ListValidationException("expectedPortions must be greater than 0");
        }
        this.expectedPortions = expectedPortions;
        this.entries = List.copyOf(entries);
    }
}
