package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.ListEntry;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.infrastructure.persistence.entity.ListEntryEntity;
import com.ania.cookbook.infrastructure.persistence.entity.SavedListEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@RequiredArgsConstructor
public class SavedListMapper {
    private final ListEntryMapper entryMapper;

    public SavedList toDomain(SavedListEntity entity) {
        List<ListEntry> domainEntries = Optional.ofNullable(entity.getEntries())
                .orElse(Collections.emptyList()).stream()
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
        SavedListEntity entity = SavedListEntity.builder()
                .listName(domain.getListName().name())
                .createdAt(domain.getCreatedAt())
                .listDescription(domain.getListDescription())
                .expectedPortions(domain.getExpectedPortions())
                .build();
        List<ListEntryEntity> entryEntities = domain.getEntries().stream()
                .map(entry -> entryMapper.toEntity(entry, entity))
                .toList();
        entity.setEntries(entryEntities);
        return entity;
    }
}
