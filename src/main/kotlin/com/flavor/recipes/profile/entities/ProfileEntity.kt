package com.flavor.recipes.profile.entities

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.sql.Timestamp

@Entity
@Table(name = "profile")
data class ProfileEntity(
    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    var id: String? = null,
    @Column(name = "name", unique = true)
    var name: String?,
    var biography: String? = null,
    @Column(name = "user_id", unique = true, nullable = false)
    var userId: String = "",
    @Column(name = "created_at", nullable = false)
    var createdAt: Timestamp,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Timestamp,
    @Column(columnDefinition = "TEXT")
    var image: String?,
    @Column(name = "total_recipes", nullable = false)
    var totalRecipes: Int = 0
)