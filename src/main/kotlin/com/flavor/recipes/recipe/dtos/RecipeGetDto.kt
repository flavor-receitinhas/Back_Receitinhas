package com.flavor.recipes.recipe.dtos

import com.flavor.recipes.recipe.entities.RecipeEntity

data class RecipeGetDto(
    val authorName: String?,
    val recipe: RecipeEntity,
)