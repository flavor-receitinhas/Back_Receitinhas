package com.flavor.recipes.recipe.entities

import com.flavor.recipes.user.entities.DifficultyRecipes
import jakarta.persistence.*


@Entity
@Table(name = "recipe")
data class RecipeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    val title: String,
    val subTitle: String,

    @ElementCollection
    val images: List<String>,

    val details: String,
    val timePrepared: Int,

    val difficultyRecipe: DifficultyRecipes,

    val portion: Int,

    @OneToOne
    @JoinColumn()
    val ingredient: RecipeIngredientEntity,

    @OneToOne
    @JoinColumn()
    val instruction: InstructionEntity,

    @OneToOne
    @JoinColumn()
    val serveFood: ServeFoodEntity,
)
