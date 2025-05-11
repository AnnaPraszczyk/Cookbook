package com.ania.cookbook.application.services.implementations.product;

import com.ania.cookbook.application.services.interfaces.product.FindProductUseCase;
import com.ania.cookbook.domain.exceptions.ProductNotFoundException;
import com.ania.cookbook.domain.exceptions.ProductValidationException;
import com.ania.cookbook.domain.repositories.product.ReadProduct;
import com.ania.cookbook.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadProductService implements FindProductUseCase {
    private final ReadProduct readProductRepository;

    @Override
    public Optional<Product> findProductByName(FindProductByName product) {
        if(isBlank(product.name())){
            throw new ProductValidationException("Product cannot be null or empty.");
        }
        return Optional.ofNullable(readProductRepository.findProductByName(product.name())
                .orElseThrow(() -> new ProductNotFoundException("Unable to find the product because it does not exist.")));
    }
    @Override
    public boolean existsProductByName(FindProductByName product) {
        if(isBlank(product.name())){
            throw new ProductValidationException("Product cannot be null or empty.");
        }
        return readProductRepository.existsProductByName(product.name());
    }
}

