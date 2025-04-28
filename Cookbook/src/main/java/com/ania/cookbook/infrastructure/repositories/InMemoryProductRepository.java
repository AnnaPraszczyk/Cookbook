package com.ania.cookbook.infrastructure.repositories;

import com.ania.cookbook.domain.repositories.product.DeleteProduct;
import com.ania.cookbook.domain.repositories.product.ReadProduct;
import com.ania.cookbook.domain.repositories.product.SaveProduct;
import com.ania.cookbook.domain.repositories.product.UpdateProduct;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Component
public class InMemoryProductRepository implements SaveProduct, ReadProduct, UpdateProduct, DeleteProduct {
    private final HashMap<UUID, ProductEntity> inMemoryRepository = new HashMap<>();

    @Override
    public ProductEntity saveProduct(ProductEntity product) {
        if (inMemoryRepository.containsKey(product.getProductId())) {
            throw new IllegalArgumentException("A product with the given ID already exists.");
        }
        inMemoryRepository.put(product.getProductId(),product);
        return product;
    }
    @Override
    public Optional<ProductEntity> findProductById(UUID id){

        return Optional.ofNullable(inMemoryRepository.get(id));
    }

    @Override
    public boolean existsProductById(UUID id){

        return inMemoryRepository.containsKey(id);
    }
    @Override
    public ProductEntity findProductByName(String name){

        return inMemoryRepository.values().stream().filter(product -> name.equals(product.getProductName()))
                .findFirst().orElse(null);
    }
    @Override
    public boolean existsProductByName(String name){
        return inMemoryRepository.values().stream().anyMatch(product -> name.equals(product.getProductName()));
    }
    @Override
    public ProductEntity updateProduct(ProductEntity product){
        inMemoryRepository.put(product.getProductId(),product);
        return product;
    }
    @Override
    public void deleteProductById(UUID id){

        inMemoryRepository.remove(id);
    }

}
