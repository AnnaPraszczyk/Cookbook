package com.ania.cookbook.infrastructure.persistence.list;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.domain.repositories.list.DeleteList;
import com.ania.cookbook.domain.repositories.list.ReadList;
import com.ania.cookbook.domain.repositories.list.SaveList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SavedListAdapterIntegrationTest {
    @Autowired
    private SaveList saveList;
    @Autowired
    private ReadList readList;
    @Autowired
    private DeleteList deleteList;

    @Test
    void saveAndReadListWithEntries() {
        ListName listName = new ListName("TestList");
        SavedList list = SavedList.builder()
                .listName(listName)
                .createdAt(Instant.now())
                .expectedPortions(4)
                .entries(List.of())
                .build();
        saveList.save(list);
        Optional<SavedList> result = readList.findByName(listName);

        assertTrue(result.isPresent());
        assertEquals(listName, result.get().getListName());
    }

    @Test
    void returnTrueWhenListExists() {
        ListName listName = new ListName("DinnerList");
        SavedList list = SavedList.builder()
                .listName(listName)
                .createdAt(Instant.now())
                .expectedPortions(2)
                .build();
        saveList.save(list);
        boolean exists = readList.existsByName(listName);

        assertTrue(exists);
    }

    @Test
    void returnFalseWhenListDoesNotExist() {
        ListName listName = new ListName("NonExistentList");
        boolean exists = readList.existsByName(listName);

        assertFalse(exists);
    }

    @Test
    void returnAllLists() {
        ListName list1 = new ListName("ListOne");
        ListName list2 = new ListName("ListTwo");
        saveList.save(SavedList.builder()
                .listName(list1)
                .createdAt(Instant.now())
                .expectedPortions(1)
                .build());
        saveList.save(SavedList.builder()
                .listName(list2)
                .createdAt(Instant.now())
                .expectedPortions(2)
                .build());
        List<ListName> allLists = readList.getAllLists();
        List<String> names = allLists.stream().map(ListName::name).toList();

        assertTrue(names.contains("ListOne"));
        assertTrue(names.contains("ListTwo"));
    }

    @Test
    void deleteListByName() {
        ListName listName = new ListName("ToDelete");
        SavedList list = SavedList.builder()
                .listName(listName)
                .createdAt(Instant.now())
                .expectedPortions(3)
                .build();
        saveList.save(list);
        assertTrue(readList.existsByName(listName));
        deleteList.delete(list);

        assertFalse(readList.existsByName(listName));
    }
}