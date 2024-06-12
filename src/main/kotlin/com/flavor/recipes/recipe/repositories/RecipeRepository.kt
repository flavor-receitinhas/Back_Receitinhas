package com.flavor.recipes.recipe.repositories

import com.flavor.recipes.recipe.entities.RecipeEntity
import org.springframework.data.domain.PageRequest
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : MongoRepository<RecipeEntity, String> {
    fun findByStatusNot(status: String, pageable: PageRequest): List<RecipeEntity>
    fun findByUserId(userId: String, pageable: PageRequest): List<RecipeEntity>
}