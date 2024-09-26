package com.flavor.recipes.recipe.services

import com.flavor.recipes.recipe.entities.RecipeEntity
import com.flavor.recipes.recipe.repositories.RecipeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.Date
import kotlin.jvm.optionals.getOrNull

@Service
class RecipeService {
    @Autowired
    lateinit var recipeRepository: RecipeRepository
    fun findByUser(
        userId: String,
        page: Int,
        isDesc: Boolean,
        sort: String
    ): List<RecipeEntity> {
        val size = 25
        val pagination = PageRequest.of(
            page,
            size,
            if (isDesc) Sort.by(sort).descending() else
                Sort.by(sort)
        )
        return recipeRepository.findByUserId(userId = userId, pagination)
    }

    fun findByStatusNot(
        status: String,
        page: Int,
        isDesc: Boolean,
        sort: String
    ): List<RecipeEntity> {
        val size = 25
        val pagination = PageRequest.of(
            page,
            size,
            if (isDesc) Sort.by(sort).descending() else
                Sort.by(sort)
        )
        return recipeRepository.findByStatusNot(status, pagination)
    }

    fun findById(id: String): RecipeEntity? {
        return recipeRepository.findById(id).getOrNull()
    }

    fun countByUser(userId: String): Int {
        return recipeRepository.countByUser(userId)
    }

    fun create(entity: RecipeEntity): RecipeEntity {
        return recipeRepository.save(
            entity.copy(
                createdAt = Timestamp.from(Date().toInstant()),
                updatedAt = Timestamp.from(Date().toInstant())
            )
        )
    }

    fun update(entity: RecipeEntity): RecipeEntity {
        return recipeRepository.save(
            entity.copy(
                updatedAt = Timestamp.from(Date().toInstant())
            )
        )
    }
}