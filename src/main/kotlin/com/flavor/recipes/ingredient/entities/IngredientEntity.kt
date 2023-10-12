package com.flavor.recipes.ingredient.entities

import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "INGREDIENT")
class IngredientEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id : Long? = null,
    @OneToMany(mappedBy = "ingredient", fetch = FetchType.EAGER)
    @JsonManagedReference
    val name : MutableList<IngredientNameTranslationEntity> = mutableListOf(),
    val description: String = "",
)