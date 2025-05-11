package com.ania.cookbook.application.services.implementations.product;

import com.ania.cookbook.application.services.interfaces.product.AddProductUseCase;
import com.ania.cookbook.application.services.interfaces.product.DeleteProductUseCase;
import com.ania.cookbook.application.services.interfaces.product.UpdateProductUseCase;
import com.ania.cookbook.domain.exceptions.ProductNotFoundException;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.repositories.product.DeleteProduct;
import com.ania.cookbook.domain.repositories.product.ReadProduct;
import com.ania.cookbook.domain.repositories.product.SaveProduct;
import com.ania.cookbook.domain.repositories.product.UpdateProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@RequiredArgsConstructor
public class ProductService implements AddProductUseCase, UpdateProductUseCase, DeleteProductUseCase {
    private final SaveProduct saveProductRepository;
    private final ReadProduct readProductRepository;
    private final UpdateProduct updateProductRepository;
    private final DeleteProduct deleteProductRepository;

    @Override
    public Product addProduct(AddProduct product) {
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
    public Product updateProductName(UpdateProductName product) {
        if(isBlank(product.name())){
            throw new ProductValidationException("Product cannot be null or empty.");
        }
        if(isBlank(product.newName())){
            throw new ProductValidationException("New product name cannot be null or empty.");
        }
        var foundProduct = readProductRepository.findProductByName(product.name())
                .orElseThrow(() -> new ProductNotFoundException("Product not found. Unable to update."));
        Product updatedProduct = Product.newProduct(foundProduct.getProductId(), product.newName());
        return updateProductRepository.updateProduct(updatedProduct);
    }

    @Override
    public void removeProduct(DeleteProductName product) {
        if(isBlank(product.name())){
            throw new ProductValidationException("Product cannot be null or empty.");
        }
        var foundProduct = readProductRepository.findProductByName(product.name())
                .orElseThrow(() -> new ProductNotFoundException("Unable to delete the product because it does not exist."));
        deleteProductRepository.deleteProductById(foundProduct.getProductId());
    }

}
