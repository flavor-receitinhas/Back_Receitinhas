package com.flavor.recipes.recipe.entities

import com.flavor.recipes.ingredient.entities.IngredientEntity
import jakarta.persistence.*

data class RecipeIngredientEntity(
    val ingredient: IngredientEntity,
    val quantity: Double,
    val unit: String
)