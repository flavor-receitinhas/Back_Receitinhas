package com.flavor.recipes.favorite.dtos

import com.flavor.recipes.favorite.entities.Favorite

data class ListFavoriteDto(
    val favorite: Favorite,
    val thumb: String?,
    val timePrepared: Int,
)