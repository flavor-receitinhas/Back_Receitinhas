package com.flavor.recipes.recipe.dtos

import com.flavor.recipes.recipe.entities.RecipeEntity
import com.flavor.recipes.recipe.entities.RecipeIngredientEntity

class RecipeCreateDto (
    val recipe: RecipeEntity,
    val ingredients: List<RecipeIngredientEntity>
)