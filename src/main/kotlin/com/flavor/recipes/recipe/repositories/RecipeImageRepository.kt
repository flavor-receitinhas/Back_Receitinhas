package com.flavor.recipes.recipe.repositories

import com.flavor.recipes.recipe.entities.RecipeImageEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipeImageRepository : JpaRepository<RecipeImageEntity, String> {
    fun countByRecipeIdAndThumb(recipeId: String, thumb: Boolean): Long
    fun countByRecipeId(recipeId: String): Long

    fun findByRecipeId(recipeId: String): List<RecipeImageEntity>
}