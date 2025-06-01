package com.ania.cookbook.infrastructure.repositories;

import com.ania.cookbook.domain.repositories.product.DeleteProduct;
import com.ania.cookbook.domain.repositories.product.ReadProduct;
import com.ania.cookbook.domain.repositories.product.SaveProduct;
import com.ania.cookbook.domain.repositories.product.UpdateProduct;
import com.ania.cookbook.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class InMemoryProductRepository implements SaveProduct, ReadProduct, UpdateProduct, DeleteProduct {
    private final HashMap<UUID, Product> inMemoryRepository = new HashMap<>();

    @Override
    public Product saveProduct(Product product) {
        inMemoryRepository.put(product.getProductId(),product);
        return product;
    }

    @Override
    public Optional<Product> findProductById(UUID id){
        return Optional.ofNullable(inMemoryRepository.get(id));
    }

    @Override
    public boolean existsProductById(UUID id){
        return inMemoryRepository.containsKey(id);
    }

    @Override
    public Optional<Product> findProductByName(String name){
        return inMemoryRepository.values().stream().filter(product -> product.getProductName().name().equals(name))
                .findFirst();
    }

    @Override
    public boolean existsProductByName(String name){
        return inMemoryRepository.values().stream().anyMatch(product -> name.equals(product.getProductName().name()));
    }

    @Override
    public Product updateProduct(Product product){
        inMemoryRepository.put(product.getProductId(),product);
        return product;
    }
    @Override
    public void deleteProductById(UUID id){
        inMemoryRepository.remove(id);
    }
}

