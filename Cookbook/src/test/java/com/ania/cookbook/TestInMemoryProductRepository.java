package com.ania.cookbook;

import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.repositories.product.DeleteProduct;
import com.ania.cookbook.domain.repositories.product.ReadProduct;
import com.ania.cookbook.domain.repositories.product.SaveProduct;
import com.ania.cookbook.domain.repositories.product.UpdateProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class TestInMemoryProductRepository implements SaveProduct, ReadProduct, UpdateProduct, DeleteProduct {
    private final HashMap<UUID, Product> inMemoryRepository = new HashMap<>();

    @Override
    public Product saveProduct(Product product) {
        if (product == null) {
            throw new ProductValidationException("Product cannot be null");}
        if (inMemoryRepository.containsKey(product.getProductId())) {
            throw new ProductValidationException("A product with the given ID already exists.");
        }
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

        return inMemoryRepository.values().stream().filter(product -> product.getProductName().equals(name))
                .findFirst();
    }
    @Override
    public boolean existsProductByName(String name){
        return inMemoryRepository.values().stream().anyMatch(product -> name.equals(product.getProductName()));
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
