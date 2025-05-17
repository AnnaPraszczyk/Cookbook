package com.ania.cookbook.application.services.implementations.ingredient;

import com.ania.cookbook.application.services.implementations.product.ProductService;
import com.ania.cookbook.application.services.interfaces.product.ProductUseCase.ProductName;
import com.ania.cookbook.domain.exceptions.IngredientValidationException;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.model.Unit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static io.micrometer.common.util.StringUtils.isBlank;

@RequiredArgsConstructor
@Service
public class IngredientService {
    private final ProductService productService;

    public Ingredient createIngredient(ProductName productName, float amount, Unit unit) {
        if (isBlank(productName.name())){
            throw new IngredientValidationException("Product name cannot be null or empty");
        }
        if (amount <= 0) {
            throw new IngredientValidationException("Amount must be greater than 0");
        }
        if (unit == null) {
            throw new IngredientValidationException("Unit cannot be null");
        }
        if (!productService.existsProductByName(productName)) {
            productService.addProduct(productName);
        }
        Product product = productService.findProductByName(productName)
                .orElseThrow(() -> new IngredientValidationException("Unable to find the product because it does not exist."));
        return Ingredient.newIngredient(product, amount, unit);
    }
}
