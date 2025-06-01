package com.ania.cookbook.web.product;

import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.exceptions.ProductNotFoundException;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.micrometer.common.util.StringUtils.isBlank;

public interface ProductMapping {

    @RequiredArgsConstructor
    @Component
    class ApiMapping {
        private final ProductService productService;

        public ProductResponse map(ProductRequest request) {
            if (isBlank(request.productName())) {
                throw new ProductValidationException("Product name cannot be null or empty");
            }
            Optional<Product> maybeProduct = productService.findProductByName(new ProductName(request.productName()));
            return maybeProduct.map(product -> new ProductResponse(product.getProductId(), product.getProductName().name()))
                    .orElseThrow(() -> new ProductNotFoundException("Unable to find the product because it does not exist."));
        }
    }

    @RequiredArgsConstructor
    @Component
    class ModelMapping {

        public ProductResponse map(Product product) {
            if (isBlank(product.getProductName().name())) {
                throw new ProductValidationException("Product cannot be null or empty");
            }
            return new ProductResponse(product.getProductId(), product.getProductName().name());
        }
    }
}
