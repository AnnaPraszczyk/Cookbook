package com.ania.cookbook.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "saved_recipe_list")
public class SavedListEntity {
    @Id
    @Column(name = "list_name", nullable = false, unique = true)
    private String listName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "list_description")
    private String listDescription;

    @Column(name = "expected_portions")
    private int expectedPortions;

    @OneToMany(mappedBy = "savedList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListEntryEntity> entries = new ArrayList<>();
}
