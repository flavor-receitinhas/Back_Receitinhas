package com.flavor.recipes.ingredient.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "INGREDIENT_NAME_TRANSLATE")
class IngredientNameTranslationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @NotNull
    val name: String = "",
    @NotNull
    val language: String = "",
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_name")
    @JsonBackReference
    val ingredient: IngredientEntity = IngredientEntity(),

    )