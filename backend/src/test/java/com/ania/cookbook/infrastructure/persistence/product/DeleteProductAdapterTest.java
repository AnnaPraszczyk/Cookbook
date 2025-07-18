package com.ania.cookbook.infrastructure.persistence.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.UUID;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteProductAdapterTest {
    @Mock
    SpringDataProductRepository jpaRepository;

    @InjectMocks
    DeleteProductAdapter adapter;

    @Test
    void callJpaDeleteById() {
        UUID id = UUID.randomUUID();
        adapter.deleteProductById(id);

        verify(jpaRepository).deleteById(id);
    }
}