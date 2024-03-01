package com.flavor.recipes.profile.entities

import jakarta.persistence.*

@Entity
@Table(name = "profile")
data class ProfileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null,
    @Column(name = "name", unique = true)
    var name: String?,
    var biography: String? = null,
    @Column(name = "user_id", unique = true, nullable = false)
    var userID: String = "",
    @Column(name = "created_at", nullable = false)
    var createdAt: Long? = null,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Long?,
    @Column(columnDefinition = "TEXT")
    var image: String?
)