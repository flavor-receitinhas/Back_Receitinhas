package com.flavor.recipes.ingredient.repository

import com.flavor.recipes.ingredient.entities.IngredientEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IngredientRepository : JpaRepository<IngredientEntity, String> {
    fun findByNameContains(name: String, page: Pageable): List<IngredientEntity>
}