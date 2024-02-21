package com.flavor.recipes.user.entities

import jakarta.persistence.*

@Entity
@Table(name = "user_preference")
data class UserPreference(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long?,
    @Column(name = "user_id", unique = true)
    val userId: String,
    val protein: List<Proteins>,
    @Column(name = "dietary_restriction")
    val dietaryRestriction: List<DietaryRestrictions>,
    @Column(name = "difficulty_recipe")
    val difficultyRecipe: List<DifficultyRecipes>
)
