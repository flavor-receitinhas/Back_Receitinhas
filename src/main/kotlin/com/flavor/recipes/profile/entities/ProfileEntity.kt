package com.flavor.recipes.profile.entities

import jakarta.persistence.*

@Entity
@Table(name = "profile")
data class ProfileEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null,
    @Column(name = "name", unique = true)
    var name: String,
    var biography: String? = null,
    @Column(name = "user_id", unique = true)
    var userID: String = "",
    @Column(name = "created_at")
    var createdAt: Long? = null,
    @Column(name = "updated_at")
    var updatedAt: Long?,
    var image: String
)