package com.flavor.recipes.favorite.entities

import jakarta.persistence.*

@Entity
@Table(name = "favorite")
data class Favorite(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(name = "recipe_id", nullable = false)
    var recipeId: String,
    @Column(name = "user_id", nullable = false)
    var userId: String,
    @Column(name = "created_at", nullable = false)
    var createdAt: Long? = null,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Long? = null,
)