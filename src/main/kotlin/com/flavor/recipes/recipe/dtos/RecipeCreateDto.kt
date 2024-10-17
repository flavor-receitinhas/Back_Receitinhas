package com.flavor.recipes.recipe.dtos

import com.flavor.recipes.recipe.entities.RecipeEntity
import com.flavor.recipes.recipe.entities.RecipeIngredientEntity
import com.flavor.recipes.recipe.entities.RecipeStatus
import com.flavor.recipes.user.entities.DifficultyRecipes

data class RecipeCreateDto(
    val title: String,
    val subTitle: String,
    val details: String,
    val timePrepared: Int,
    val difficultyRecipe: DifficultyRecipes,
    val portion: Int,
    val instruction: String,
    val serveFood: String,
    val ingredients: List<RecipeIngredientEntity>,
)