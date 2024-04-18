package com.flavor.recipes.favorite.repositories

import com.flavor.recipes.favorite.entities.Favorite
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FavoriteRepository : JpaRepository<Favorite, Long> {
    fun findByUserId(userId: String, pageable: PageRequest): List<Favorite>
}