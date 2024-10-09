package com.flavor.recipes.recipe.repositories

import com.flavor.recipes.recipe.entities.RecipeEntity
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : JpaRepository<RecipeEntity, String> {
    fun findByStatusNot(status: String, pageable: PageRequest): List<RecipeEntity>
    fun findByUserId(userId: String, pageable: PageRequest): List<RecipeEntity>

    @Query("SELECT COUNT(id) FROM RecipeEntity WHERE userId = :userId")
    fun countByUser(userId: String): Int
}