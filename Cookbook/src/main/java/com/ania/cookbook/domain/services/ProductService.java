package com.ania.cookbook.domain.services;

import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.ania.cookbook.infrastructure.repositories.InMemoryProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class ProductService {

    private final InMemoryProductRepository productRepository;
    @Autowired
    public ProductService(InMemoryProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductEntity saveProduct(ProductEntity product) {
        if(product == null || product.getProductId() == null){
            throw new IllegalArgumentException("Unable to save a product without Id.");
        }
        if(product.getProductName() == null || product.getProductName().isEmpty()){
            throw new IllegalArgumentException("Unable to save a product without name.");
        }
        if (productRepository.existsProductById(product.getProductId())) {
            throw new IllegalArgumentException("A product with the given ID already exists.");
        }
        return productRepository.saveProduct(product);
    }

    public ProductEntity updateProduct(ProductEntity product) {
        if (!productRepository.existsProductById(product.getProductId())) {
            throw new IllegalArgumentException("Unable to update the product because it does not exist.");
        }
        return productRepository.updateProduct(product);
    }

    public void deleteProductById(UUID id) {
        if (!productRepository.existsProductById(id)) {
            throw new IllegalArgumentException("Unable to delete the product because it does not exist.");
        }
        productRepository.deleteProductById(id);
    }
}
