package com.flavor.recipes.recipe.entities

import com.flavor.recipes.user.entities.DifficultyRecipes
import jakarta.persistence.*
import org.springframework.data.annotation.Id

@Entity
@Table(name = "recipe")
data class RecipeEntity(
    @Id
    val id: String,

    val title: String,
    val subTitle: String,

    @ElementCollection
    val images: List<String>,

    val details: String,
    val timePrepared: Int,


    val difficultyRecipe: DifficultyRecipes,

    val portion: Int,


    val ingredient: RecipeIngredientEntity,


    val instruction: InstructionEntity,


    val serveFood: ServeFoodEntity,
)
