package com.ania.cookbook.domain.repositories.product;

import com.ania.cookbook.domain.model.Product;
import com.ania.cookbook.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface ProductRepository extends CrudRepository<Product, UUID> {

    ProductEntity saveProduct(ProductEntity product);
    Optional<ProductEntity>  findProductById(UUID id);
    boolean existsProductById(UUID id);

    ProductEntity findProductByName(String name);

    boolean existsProductByName(String name);

    ProductEntity updateProduct(ProductEntity product);

    void deleteProductById(UUID id);

}
