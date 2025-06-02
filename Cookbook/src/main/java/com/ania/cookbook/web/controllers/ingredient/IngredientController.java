package com.ania.cookbook.web.controllers.ingredient;

import com.ania.cookbook.application.services.implementations.ingredient.IngredientService;
import com.ania.cookbook.domain.model.Ingredient;
import com.ania.cookbook.web.ingredient.IngredientRequest;
import com.ania.cookbook.web.ingredient.IngredientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {
    private final IngredientService ingredientService;
    private static final Logger log = LoggerFactory.getLogger(IngredientController.class);

    @PostMapping
    public ResponseEntity<IngredientResponse> createIngredient(@RequestBody IngredientRequest request) {
        log.info("Received request: {}", request);

        Ingredient ingredient = ingredientService.createIngredient(
                request.productName(),
                request.amount(),
                request.unit()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(ingredient));
    }

    private IngredientResponse mapToResponse(Ingredient ingredient) {
        return new IngredientResponse(
                ingredient.getProduct().getProductId(),
                ingredient.getProduct().getProductName(),
                ingredient.getAmount(),
                ingredient.getUnit()
        );
    }


}
