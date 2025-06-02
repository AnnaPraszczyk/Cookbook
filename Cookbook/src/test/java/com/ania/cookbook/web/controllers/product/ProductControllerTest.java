package com.ania.cookbook.web.controllers.product;

import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.web.product.ProductRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addProduct() throws Exception {
        ProductName productName = new ProductName("New Product");
        ProductRequest request = new ProductRequest(productName);
        Product product = Product.newProduct(UUID.randomUUID(),productName);

        Mockito.when(productService.addProduct(Mockito.any(ProductName.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName.name").value("New Product"));
    }

    @Test
    void getProductByName() throws Exception {
        ProductName productName = new ProductName("Existing Product");
                        Product product = Product.newProduct(UUID.randomUUID(), productName);

        Mockito.when(productService.findProductByName(Mockito.any(ProductName.class))).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/{productName}", productName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName.name").value(productName.name()));
    }

    @Test
    void shouldReturnNotFoundForNonExistingProduct() throws Exception {
        Mockito.when(productService.findProductByName(Mockito.any(ProductName.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/products/UnknownProduct"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateProduct() throws Exception {
            ProductName oldName = new ProductName("Old Product");
            ProductName newName = new ProductName("Updated Product");
            ProductRequest request = new ProductRequest(newName);
            Product updatedProduct = Product.newProduct(UUID.randomUUID(), newName);

            Mockito.when(productService.updateProductName(Mockito.any(ProductName.class), Mockito.any(ProductName.class))).thenReturn(updatedProduct);

            mockMvc.perform(put("/products/{productName}", oldName)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productName.name").value(newName.name()));
        }

    @Test
    void deleteProduct() throws Exception{
        ProductName productName = new ProductName("To Be Deleted");

        Mockito.doNothing().when(productService).removeProduct(Mockito.any(ProductName.class));

        mockMvc.perform(delete("/products/{productName}", productName))
                .andExpect(status().isNoContent());
    }
}