package com.ania.cookbook.infrastructure.mapper;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.web.list.ListEntryResponse;
import com.ania.cookbook.web.list.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ListResponseMapper {
    private final ListEntryResponseMapper entryMapper;

    public ListResponse from(SavedList savedList) {
        List<ListEntryResponse> entryResponses = entryMapper.toResponseList(savedList.getEntries());
        return ListResponse.builder()
                .listName(savedList.getListName())
                .listDescription(savedList.getListDescription())
                .expectedPortions(savedList.getExpectedPortions())
                .recipes(entryResponses)
                .build();
    }
}
