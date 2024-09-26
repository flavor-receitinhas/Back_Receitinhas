package com.flavor.recipes.favorite.entities

import jakarta.persistence.*
import java.sql.Timestamp

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
    @Column(nullable = false)
    var name: String,
    @Column(name = "created_at", nullable = false)
    var createdAt: Timestamp,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Timestamp,
)