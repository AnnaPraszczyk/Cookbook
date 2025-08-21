package com.ania.cookbook.web.mappers;
import com.ania.cookbook.domain.exceptions.RecipeValidationException;
import com.ania.cookbook.domain.model.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryResolver {
    public Category resolve(String categoryValue) {
        try{
            return Category.fromDisplayName(categoryValue.trim());
        } catch (RecipeValidationException e){
            throw new RecipeValidationException("Invalid category: " + categoryValue);
        }
    }
}
