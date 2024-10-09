package com.flavor.recipes.recipe.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "recipe_image")
data class RecipeImageEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    @Column(name = "recipe_id", nullable = false)
    val recipeId: String,
    val name: String = "",
    val type: String = "",
    val size: Long = 0,
    val link: String = "",
    val thumb: Boolean = false
)