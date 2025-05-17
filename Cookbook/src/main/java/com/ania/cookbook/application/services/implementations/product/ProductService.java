package com.ania.cookbook.application.services.implementations.product;

import com.ania.cookbook.application.services.interfaces.product.ProductUseCase;
import com.ania.cookbook.domain.exceptions.ProductNotFoundException;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.repositories.product.DeleteProduct;
import com.ania.cookbook.domain.repositories.product.ReadProduct;
import com.ania.cookbook.domain.repositories.product.SaveProduct;
import com.ania.cookbook.domain.repositories.product.UpdateProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import static io.micrometer.common.util.StringUtils.isBlank;


@Service
@RequiredArgsConstructor
public class ProductService implements ProductUseCase {
    private final SaveProduct saveProductRepository;
    private final ReadProduct readProductRepository;
    private final UpdateProduct updateProductRepository;
    private final DeleteProduct deleteProductRepository;

    @Override
    public Product addProduct(ProductName product) {
        if(isBlank(product.name())){
            throw new ProductValidationException("Product cannot be null or empty.");
        }
        if (readProductRepository.existsProductByName(product.name())) {
            throw new ProductValidationException("A product already exists.");
        }
        var newProduct = Product.newProduct(UUID.randomUUID(), product.name());
        return saveProductRepository.saveProduct(newProduct);
    }

    @Override
    public Optional<Product> findProductByName(ProductName product) {
        if(isBlank(product.name())){
            throw new ProductValidationException("Product cannot be null or empty.");
        }
        return Optional.ofNullable(readProductRepository.findProductByName(product.name())
                .orElseThrow(() -> new ProductNotFoundException("Unable to find the product because it does not exist.")));
    }
    @Override
    public boolean existsProductByName(ProductName product) {
        if(isBlank(product.name())){
            throw new ProductValidationException("Product cannot be null or empty.");
        }
        return readProductRepository.existsProductByName(product.name());
    }

    @Override
    public Product updateProductName(ProductName product, ProductName newProduct) {
        if(isBlank(product.name())){
            throw new ProductValidationException("Product cannot be null or empty.");
        }
        if(isBlank(newProduct.name())){
            throw new ProductValidationException("New product name cannot be null or empty.");
        }
        var foundProduct = readProductRepository.findProductByName(product.name())
                .orElseThrow(() -> new ProductNotFoundException("Product not found. Unable to update."));
        Product updatedProduct = Product.newProduct(foundProduct.getProductId(), newProduct.name());
        return updateProductRepository.updateProduct(updatedProduct);
    }

    @Override
    public void removeProduct(ProductName productName) {
        if(isBlank(productName.name())){
            throw new ProductValidationException("Product cannot be null or empty.");
        }
        var foundProduct = readProductRepository.findProductByName(productName.name())
                .orElseThrow(() -> new ProductNotFoundException("Unable to delete the product because it does not exist."));
        deleteProductRepository.deleteProductById(foundProduct.getProductId());
    }

}
