package com.flavor.recipes.recipe.repositories

import com.flavor.recipes.recipe.entities.RecipeIngredientEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipeIngredientRepository : JpaRepository<RecipeIngredientEntity, String> {
    fun findByRecipeId(recipeId: String): List<RecipeIngredientEntity>
}