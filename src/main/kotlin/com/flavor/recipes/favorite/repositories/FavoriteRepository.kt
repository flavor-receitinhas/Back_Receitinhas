package com.flavor.recipes.favorite.repositories

import com.flavor.recipes.favorite.entities.Favorite
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FavoriteRepository : JpaRepository<Favorite, Long> {
    @Query("Select c from Favorite c where c.name like %:search% and c.userId = :userId")
    fun findByUserIdAndSearch(userId: String, search: String, pageable: PageRequest): List<Favorite>
    fun findByUserId(userId: String, pageable: PageRequest): List<Favorite>

}