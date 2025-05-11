package com.ania.cookbook.application.services.implementations.conversion;

import com.ania.cookbook.application.services.interfaces.conversion.Converter;
import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConversionService {
    private final Converter<ProductEntity, Product> productConverter;
    private final Converter<Product,ProductEntity> productEntityConverter;
    private final Converter<RecipeEntity, Recipe> recipeConverter;
    private final Converter<Recipe,RecipeEntity> recipeEntityConverter;

    public Product convertProductToDomain(ProductEntity entity) {
        return productConverter.convert(entity);
    }

    public ProductEntity convertProductToEntity(Product product) {
        return productEntityConverter.convert(product);
    }

    public Recipe convertRecipeToDomain(RecipeEntity entity) {
        return recipeConverter.convert(entity);
    }

    public RecipeEntity convertToEntity(Recipe recipe) {
        return recipeEntityConverter.convert(recipe);
    }
}
