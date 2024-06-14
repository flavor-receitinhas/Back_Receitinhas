package com.flavor.recipes.user.entities

data class UserEntity(
    val id: String,
    val email: String,
    val emailVerified: Boolean,
    val signProvider: String,
)