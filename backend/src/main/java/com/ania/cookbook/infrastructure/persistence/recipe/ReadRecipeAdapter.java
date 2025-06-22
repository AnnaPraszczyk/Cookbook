package com.ania.cookbook.infrastructure.persistence.recipe;

import com.ania.cookbook.domain.model.Category;
import com.ania.cookbook.domain.model.Recipe;
import com.ania.cookbook.domain.repositories.recipe.ReadRecipe;
import com.ania.cookbook.infrastructure.mapper.RecipeMapper;
import com.ania.cookbook.infrastructure.persistence.entity.RecipeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
@RequiredArgsConstructor
public class ReadRecipeAdapter implements ReadRecipe {
    private final SpringDataRecipeRepository jpaRepository;
    private final RecipeMapper recipeMapper;
    private final EntityManager em;


    @Override
    public Optional<Recipe> findRecipeById(UUID id) {
        return jpaRepository.findById(id)
                .map(recipeMapper::toDomain);
    }

    @Override
    public boolean existsRecipeById(UUID id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public List<Recipe> findRecipeByName(String name) {
        return jpaRepository.findByRecipeNameContainingIgnoreCase(name)
                .stream()
                .map(recipeMapper::toDomain)
                .toList();
    }

    @Override
    public Page<Recipe> findRecipeByName(String name, Pageable pageable) {
        return jpaRepository.findByRecipeNameContainingIgnoreCase(name, pageable)
                .map(recipeMapper::toDomain);
    }

    @Override
    public boolean existsRecipeByName(String name) {
        return jpaRepository.existsByRecipeNameIgnoreCase(name);
    }

    @Override
    public List<Recipe> findRecipeByCategory(Category category) {
        return jpaRepository.findByCategory(category)
                .stream()
                .map(recipeMapper::toDomain)
                .toList();
    }

    @Override
    public Page<Recipe> findRecipeByCategory(Category category, Pageable pageable) {
        return jpaRepository.findByCategory(category, pageable)
                .map(recipeMapper::toDomain);
    }

    @Override
    public List<Recipe> findRecipeByTag(String tag) {
        return jpaRepository.findByTagEquals(tag)
                .stream()
                .map(recipeMapper::toDomain)
                .toList();
    }

    @Override
    public Page<Recipe> findRecipeByTag(String tag, Pageable pageable) {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<RecipeEntity> cq = cb.createQuery(RecipeEntity.class);
            Root<RecipeEntity> root = cq.from(RecipeEntity.class);
            Join<RecipeEntity, String> joinTags = root.join("tags");
            cq.select(root).distinct(true)
                    .where(cb.like(cb.lower(joinTags), "%" + tag.toLowerCase() + "%"));

            TypedQuery<RecipeEntity> tq = em.createQuery(cq);
            tq.setFirstResult((int)pageable.getOffset());
            tq.setMaxResults(pageable.getPageSize());
            List<RecipeEntity> content = tq.getResultList();

            CriteriaQuery<Long> countCq = cb.createQuery(Long.class);
            Root<RecipeEntity> countRoot = countCq.from(RecipeEntity.class);
            Join<RecipeEntity, String> countTags = countRoot.join("tags");
            countCq.select(cb.countDistinct(countRoot))
                    .where(cb.like(cb.lower(countTags), "%" + tag.toLowerCase() + "%"));
            Long total = em.createQuery(countCq).getSingleResult();

            return new PageImpl<>(
                    content.stream().map(recipeMapper::toDomain).toList(),
                    pageable,
                    total
            );
    }
}
