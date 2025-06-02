package com.ania.cookbook.web.controllers.product;

import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.web.product.ProductRequest;
import com.ania.cookbook.web.product.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest request) {
        var product = productService.addProduct(request.productName());
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(product));
    }

    @GetMapping("/{productName}")
    public ResponseEntity<ProductResponse> getProductByName(@PathVariable ProductName productName) {
        return productService.findProductByName(productName)
                .map(product -> ResponseEntity.ok(mapToResponse(product)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{productName}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable ProductName productName, @RequestBody ProductRequest request) {
        var updatedProduct = productService.updateProductName(productName, request.productName());
        return ResponseEntity.ok(mapToResponse(updatedProduct));
    }

    @DeleteMapping("/{productName}")
    public ResponseEntity<Void> deleteProduct(@PathVariable ProductName productName) {
        productService.removeProduct(productName);
        return ResponseEntity.noContent().build();
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(product.getProductId(), product.getProductName());
    }

}
