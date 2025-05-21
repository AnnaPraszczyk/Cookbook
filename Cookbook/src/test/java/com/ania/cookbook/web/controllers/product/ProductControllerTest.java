package com.ania.cookbook.web.controllers.product;

import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.web.product.ProductMapping.ModelMapping;
import com.ania.cookbook.web.product.ProductMapping.ApiMapping;
import com.ania.cookbook.web.product.ProductRequest;
import com.ania.cookbook.web.product.ProductResponse;
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
    @MockitoBean
    private ApiMapping apiMapping;
    @MockitoBean
    private ModelMapping modelMapping;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addProduct() throws Exception {
        ProductRequest request = new ProductRequest("New Product");
        Product product = Product.newProduct(UUID.randomUUID(), "New Product");
        ProductResponse response = new ProductResponse(product.getProductId(), product.getProductName());

        Mockito.when(productService.addProduct(Mockito.any())).thenReturn(product);
        Mockito.when(modelMapping.map(product)).thenReturn(response);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").exists())
                .andExpect(jsonPath("$.productName").value("New Product"));
    }

    @Test
    void getProductByName() throws Exception {
        String productName = "Existing Product";
        Product product = Product.newProduct(UUID.randomUUID(), productName);
        ProductResponse response = new ProductResponse(product.getProductId(), product.getProductName());

        Mockito.when(productService.findProductByName(Mockito.any())).thenReturn(java.util.Optional.of(product));
        Mockito.when(modelMapping.map(product)).thenReturn(response);

        mockMvc.perform(get("/products/{productName}", productName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").exists())
                .andExpect(jsonPath("$.productName").value(productName));
    }

    @Test
    void updateProduct() throws Exception {
            String oldName = "Old Product";
            String newName = "Updated Product";
            ProductRequest request = new ProductRequest(newName);
            Product updatedProduct = Product.newProduct(UUID.randomUUID(), newName);
            ProductResponse response = new ProductResponse(updatedProduct.getProductId(), updatedProduct.getProductName());

            Mockito.when(productService.updateProductName(Mockito.any(), Mockito.any())).thenReturn(updatedProduct);
            Mockito.when(modelMapping.map(updatedProduct)).thenReturn(response);

            mockMvc.perform(put("/products/{productName}", oldName)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productName").value(newName));
        }

    @Test
    void deleteProduct() throws Exception{
        String productName = "To Be Deleted";

        Mockito.doNothing().when(productService).removeProduct(Mockito.any());

        mockMvc.perform(delete("/products/{productName}", productName))
                .andExpect(status().isNoContent());
    }
}