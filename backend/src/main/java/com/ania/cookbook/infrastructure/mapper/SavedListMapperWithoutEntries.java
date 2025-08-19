package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.infrastructure.persistence.entity.SavedListEntity;
import org.springframework.stereotype.Component;
import java.util.Collections;

@Component
public class SavedListMapperWithoutEntries {
    public SavedList toDomainWithoutEntries(SavedListEntity entity) {
        return SavedList.builder()
                .listName(new ListName(entity.getListName()))
                .createdAt(entity.getCreatedAt())
                .listDescription(entity.getListDescription())
                .expectedPortions(entity.getExpectedPortions())
                .entries(Collections.emptyList())
                .build();
    }
}
