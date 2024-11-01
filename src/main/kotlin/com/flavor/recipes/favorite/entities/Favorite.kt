package com.flavor.recipes.favorite.entities

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.sql.Timestamp

@Entity
@Table(name = "favorite")
data class Favorite(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    var id: String? = null,
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