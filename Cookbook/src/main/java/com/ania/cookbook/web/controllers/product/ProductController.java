package com.ania.cookbook.web.controllers.product;

import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.exceptions.ProductNotFoundException;
import com.ania.cookbook.web.product.ProductMapping;
import com.ania.cookbook.web.product.ProductMapping.ApiMapping;
import com.ania.cookbook.web.product.ProductMapping.ModelMapping;

import com.ania.cookbook.web.product.ProductRequest;
import com.ania.cookbook.web.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ApiMapping apiMapping;
    private final ProductMapping.ModelMapping modelMapping;

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest request) {
        var product = productService.addProduct(new ProductName(request.productName()));
        return ResponseEntity.ok(modelMapping.map(product));
    }

    @GetMapping("/{productName}")
    public ResponseEntity<ProductResponse> getProductByName(@PathVariable String productName) {
        var product = productService.findProductByName(new ProductName(productName))
                .orElseThrow(() -> new ProductNotFoundException("Product not found."));
        return ResponseEntity.ok(modelMapping.map(product));
    }

    @PutMapping("/{productName}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable String productName, @RequestBody ProductRequest request) {
        var updatedProduct = productService.updateProductName(new ProductName(productName), new ProductName(request.productName()));
        return ResponseEntity.ok(modelMapping.map(updatedProduct));
    }

    @DeleteMapping("/{productName}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productName) {
        productService.removeProduct(new ProductName(productName));
        return ResponseEntity.noContent().build();
    }
}
