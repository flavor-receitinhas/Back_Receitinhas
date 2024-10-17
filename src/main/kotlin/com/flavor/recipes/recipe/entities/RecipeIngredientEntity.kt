package com.flavor.recipes.recipe.entities

import com.flavor.recipes.ingredient.entities.IngredientEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator

@Entity(name = "recipe_ingredient")
data class RecipeIngredientEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    val ingredientId: String,
    val recipeId: String,
    val quantity: Double,
    val unit: String
)