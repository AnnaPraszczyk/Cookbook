package com.ania.cookbook.application.services;

import com.ania.cookbook.domain.exceptions.ProductNotFoundException;
import com.ania.cookbook.domain.repositories.product.ReadProduct;
import com.ania.cookbook.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadProductService {
    private final ReadProduct readProductRepository;

    public Product findProductById(UUID id) {

        return readProductRepository.findProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
    }

    public boolean existsProductById(UUID id) {
        return readProductRepository.existsProductById(id);
    }

    public Product findProductByName(String name) {

        return readProductRepository.findProductByName(name)
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
    }

    public boolean existsProductByName(String name) {
        return readProductRepository.existsProductByName(name);
    }
}
