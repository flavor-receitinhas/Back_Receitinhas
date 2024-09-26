package com.flavor.recipes.recipe.entities

import com.flavor.recipes.recipe.converters.RecipeIngredientEntitySetConverter
import com.flavor.recipes.user.entities.DifficultyRecipes
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.sql.Timestamp


@Entity
@Table(name = "recipe")
data class RecipeEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    val id: String? = null,
    val title: String,
    val userId: String,
    @Column(name = "sub_title", nullable = false)
    val subTitle: String,
    val images: List<String>,
    val thumb: String,
    val details: String,
    @Column(name = "time_prepared", nullable = false)
    val timePrepared: Int,
    @Column(name = "difficulty_recipe", nullable = false)
    val difficultyRecipe: DifficultyRecipes,
    val portion: Int,
    val instruction: String,
    @Column(name = "serve_food", nullable = false)
    val serveFood: String,
    @Lob
    @Convert(converter = RecipeIngredientEntitySetConverter::class)
    @Column(name = "ingredients", columnDefinition = "text", nullable = false)
    val ingredients: List<RecipeIngredientEntity>,
    val status: RecipeStatus,
    @Column(name = "created_at", nullable = false)
    var createdAt: Timestamp,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Timestamp,
)