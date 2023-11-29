package com.flavor.recipes.ingredient.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import com.flavor.recipes.recipe.entities.RecipeIngredientEntity
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "ingredient")
data class IngredientEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,
    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY)
    @JsonManagedReference
    val name : MutableList<IngredientNameTranslationEntity> = mutableListOf(),
    val description: String = "",
    @ManyToOne
    val recipeIngredient : RecipeIngredientEntity,
)