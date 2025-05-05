package com.ania.cookbook.application.services;

import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.repositories.product.DeleteProduct;
import com.ania.cookbook.domain.repositories.product.ReadProduct;
import com.ania.cookbook.domain.repositories.product.SaveProduct;
import com.ania.cookbook.domain.repositories.product.UpdateProduct;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final SaveProduct saveProductRepository;
    private final ReadProduct readProductRepository;
    private final UpdateProduct updateProductRepository;
    private final DeleteProduct deleteProductRepository;

    public Product saveProduct(Product product) {
        validateProductBeforeSaving(product);
        return saveProductRepository.saveProduct(product);
    }
    private void validateProductBeforeSaving(Product product) {
        if(product == null || product.getProductId()==null){
            throw new ProductValidationException("Product id cannot be null");
        }
        if(product.getProductName() == null || product.getProductName().isBlank()){
            throw new ProductValidationException("Product name cannot be null or empty");
        }
        if (readProductRepository.existsProductById(product.getProductId())) {
            throw new ProductValidationException("A product with the given id already exists.");
        }
    }

    public Product updateProduct(Product product) {
        if (!readProductRepository.existsProductById(product.getProductId())) {
            throw new IllegalArgumentException("Unable to update the product because it does not exist.");
        }
        return updateProductRepository.updateProduct(product);
    }

    public void deleteProductById(UUID id) {
        if (!readProductRepository.existsProductById(id)) {
            throw new IllegalArgumentException("Unable to delete the product because it does not exist.");
        }
        deleteProductRepository.deleteProductById(id);
    }

    private static ProductEntity toEntity(Product product) {
        if (product == null) {throw new IllegalArgumentException("Product cannot be null");}
        return ProductEntity.newProductEntity(product.getProductId(), product.getProductName());
    }

    private static Product toDomain(ProductEntity productEntity) {
        if (productEntity == null) {throw new IllegalArgumentException("ProductEntity cannot be null");}
        return Product.newProduct(productEntity.getProductId(), productEntity.getProductName());
    }

}
