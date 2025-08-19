package com.ania.cookbook.infrastructure.persistence.list;
import com.ania.cookbook.application.services.implementations.list.ListName;
import com.ania.cookbook.domain.model.SavedList;
import com.ania.cookbook.domain.repositories.list.ReadList;
import com.ania.cookbook.domain.repositories.list.SaveList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SavedListAdapterIntergrationTest {
    @Autowired
    private SaveList saveList;

    @Autowired
    private ReadList readList;

    @Test
    void shouldSaveAndReadListWithEntries() {
        // given
        SavedList list = SavedList.builder()
                .listName(new ListName("TestList"))
                .createdAt(Instant.now())
                .expectedPortions(4)
                .entries(List.of(/* dodaj przykładowe ListEntry */))
                .build();
        // when
        saveList.save(list);
        Optional<SavedList> result = readList.findByName(new ListName("TestList"));

        // then
        assertTrue(result.isPresent());
        assertFalse(result.get().getEntries().isEmpty());
    }


    }