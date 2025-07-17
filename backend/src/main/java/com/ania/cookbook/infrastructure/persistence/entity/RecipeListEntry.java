package com.ania.cookbook.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "recipe_list_entry")
public class RecipeListEntry {
    @Id
    @GeneratedValue
    @Column(name="entry_id")
    private UUID entryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private RecipeEntity recipe;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_name", nullable = false)
    private SavedRecipeList savedList;
    @Column
    private int portions;
}
