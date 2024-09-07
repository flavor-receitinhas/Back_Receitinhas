package com.flavor.recipes.ingredient.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.flavor.recipes.recipe.entities.RecipeIngredientEntity
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.jetbrains.annotations.NotNull

@Entity(name = "ingredient")
data class IngredientEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    val name: String,
    val description: String = ""
)