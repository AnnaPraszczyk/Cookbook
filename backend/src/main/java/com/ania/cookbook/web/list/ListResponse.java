package com.ania.cookbook.web.list;
import com.ania.cookbook.application.services.implementations.list.ListName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ListResponse {
    private ListName listName;
    private String listDescription;
    private Integer expectedPortions;
    private List<ListEntryResponse> recipes;
}
