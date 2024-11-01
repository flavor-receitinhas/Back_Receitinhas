package com.flavor.recipes.favorite.dtos

data class FavoriteCheckDto(
    val exists: Boolean,
    val favoriteId: String?,
)