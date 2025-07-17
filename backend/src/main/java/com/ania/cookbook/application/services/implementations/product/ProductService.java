package com.ania.cookbook.application.services.implementations.product;

import com.ania.cookbook.application.services.interfaces.product.ProductUseCase;
import com.ania.cookbook.domain.exceptions.ProductNotFoundException;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.repositories.product.DeleteProduct;
import com.ania.cookbook.domain.repositories.product.ReadProduct;
import com.ania.cookbook.domain.repositories.product.SaveProduct;
import com.ania.cookbook.domain.repositories.product.UpdateProduct;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {
    private final SaveProduct saveProductRepository;
    private final ReadProduct readProductRepository;
    private final UpdateProduct updateProductRepository;
    private final DeleteProduct deleteProductRepository;

    @Override
    public Product addProduct(ProductName product) {
        return readProductRepository.findProductByName(product.name())
                .orElseGet(() -> {
                    UUID id = UUID.randomUUID();
                    var newProduct = Product.newProduct(id, product);
                    return saveProductRepository.saveProduct(newProduct);
                });
    }

    @Override
    public Optional<Product> findProductByName(ProductName product) {
        return readProductRepository.findProductByName(product.name());
    }

    @Override
    public boolean existsProductByName(ProductName product) {
        return readProductRepository.existsProductByName(product.name());
    }

    @Override
    public List<Product> findAll() {
        return readProductRepository.findAll();
    }

    @Override
    public Product updateProductName(ProductName product, ProductName newName) {
        var existing = readProductRepository.findProductByName(newName.name());
        if (existing.isPresent()) {
            return existing.get();
        }

        var found = readProductRepository.findProductByName(product.name())
                .orElseThrow(() -> new ProductNotFoundException("Product not found. Unable to update."));

        Product updated = Product.newProduct(found.getProductId(), newName);
        return updateProductRepository.updateProduct(updated);

    }

    @Override
    public void removeProduct(ProductName productName) {
        var foundProduct = readProductRepository.findProductByName(productName.name())
                .orElseThrow(() -> new ProductNotFoundException("Unable to delete the product because it does not exist."));
        deleteProductRepository.deleteProductById(foundProduct.getProductId());
    }

}
