package com.flavor.recipes.recipe.entities

import com.flavor.recipes.ingredient.entities.IngredientEntity
import jakarta.persistence.*

@Entity
data class RecipeIngredientEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val ingredientEntity: List<IngredientEntity>,

    @OneToOne
    val recipes : RecipeEntity,
)