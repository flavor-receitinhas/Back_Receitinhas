package com.flavor.recipes.recipe.dtos

data class RecipeIngredientCreateDto(
    val ingredientId: String,
    val quantity: Double,
    val unit: String
)