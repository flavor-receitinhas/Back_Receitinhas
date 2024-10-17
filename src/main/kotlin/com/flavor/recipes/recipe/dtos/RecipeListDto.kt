package com.flavor.recipes.recipe.dtos

data class RecipeListDto(
    val thumb: String?,
    val timePrepared: Int,
    val recipeId: String,
    val title: String,
)