package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.infrastructure.persistence.entity.ListEntryEntity;
import com.ania.cookbook.infrastructure.persistence.entity.SavedListEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class SavedListMapper {
    private final ListEntryMapper entryMapper;

    public SavedList toDomain(SavedListEntity entity) {
        List<ListEntry> domainEntries = entity.getEntries().stream()
                .map(entryMapper::toDomain)
                .toList();

        return SavedList.builder()
                .listName(new ListName(entity.getListName()))
                .createdAt(entity.getCreatedAt())
                .listDescription(entity.getListDescription())
                .expectedPortions(entity.getExpectedPortions())
                .entries(domainEntries)
                .build();
    }

    public SavedListEntity toEntity(SavedList domain) {
        List<ListEntryEntity> entryEntities = domain.getEntries().stream()
                .map(entryMapper::toEntity)
                .toList();
        SavedListEntity entity = SavedListEntity.builder()
                .listName(domain.getListName().name())
                .createdAt(domain.getCreatedAt())
                .listDescription(domain.getListDescription())
                .expectedPortions(domain.getExpectedPortions())
                .entries(entryEntities)
                .build();
        entity.getEntries().forEach(e -> e.setSavedList(entity));
        return entity;
    }
}
