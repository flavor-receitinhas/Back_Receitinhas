package com.flavor.recipes.recipe.services

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.recipe.dtos.RecipeCreateDto
import com.flavor.recipes.recipe.dtos.RecipeUpdateDto
import com.flavor.recipes.recipe.entities.RecipeEntity
import com.flavor.recipes.recipe.entities.RecipeImageEntity
import com.flavor.recipes.recipe.entities.RecipeStatus
import com.flavor.recipes.recipe.repositories.RecipeBucketRepository
import com.flavor.recipes.recipe.repositories.RecipeImageRepository
import com.flavor.recipes.recipe.repositories.RecipeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.util.Date
import kotlin.jvm.optionals.getOrNull

@Service
class RecipeService {
    @Autowired
    lateinit var recipeImageRepository: RecipeImageRepository

    @Autowired
    lateinit var recipeBucketRepository: RecipeBucketRepository

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

    fun findAllImages(recipeId: String): List<RecipeImageEntity> {
        return recipeImageRepository.findByRecipeId(recipeId)
    }

    fun countByUser(userId: String): Int {
        return recipeRepository.countByUser(userId)
    }

    fun create(dto: RecipeCreateDto, userId: String): RecipeEntity {
        return recipeRepository.save(
            RecipeEntity(
                details = dto.details,
                difficultyRecipe = dto.difficultyRecipe,
                ingredients = dto.ingredients,
                instruction = dto.instruction,
                portion = dto.portion,
                serveFood = dto.serveFood,
                subTitle = dto.subTitle,
                timePrepared = dto.timePrepared,
                title = dto.title,
                status = dto.status,
                userId = userId,
                createdAt = Timestamp.from(Date().toInstant()),
                updatedAt = Timestamp.from(Date().toInstant())
            )
        )
    }

    fun update(dto: RecipeUpdateDto, id: String): RecipeEntity {
        val recipe = recipeRepository.findById(id).getOrNull()
            ?: throw BusinessException("Receita não encontrada")
        if (recipe.status == RecipeStatus.blocked) {
            throw BusinessException("Está receita não pode ser alterada.")
        }
        return recipeRepository.save(
            recipe.copy(
                details = dto.details,
                difficultyRecipe = dto.difficultyRecipe,
                ingredients = dto.ingredients,
                instruction = dto.instruction,
                portion = dto.portion,
                serveFood = dto.serveFood,
                subTitle = dto.subTitle,
                timePrepared = dto.timePrepared,
                title = dto.title,
                status = dto.status,
                updatedAt = Timestamp.from(Date().toInstant())
            )
        )
    }

    fun createImage(id: String, file: MultipartFile, isThumb: Boolean = false) {
        val recipe = recipeRepository.findById(id).getOrNull()
            ?: throw BusinessException("Receita não encontrada")

        if (isThumb) {
            val quantity = recipeImageRepository.countByRecipeIdAndThumb(recipe.id!!, true)
            if (quantity > 1) throw BusinessException("Receita só pode ter uma capa")
        } else {
            val quantity = recipeImageRepository.countByRecipeId(recipe.id!!)
            if (quantity > 10) throw BusinessException("Receita só pode ter 10 imagens")
        }

        val image = recipeImageRepository.save(
            RecipeImageEntity(
                recipeId = recipe.id
            )
        )
        recipeBucketRepository.saveImage(image.id!!, file)
        val linkImage = recipeBucketRepository.getLinkImage(image.id)
        recipeImageRepository.save(
            image.copy(
                size = file.size,
                name = file.name,
                link = linkImage,
                type = file.contentType ?: "n/a",
                thumb = isThumb
            )
        )
    }


    fun deleteImage(id: String) {
        recipeImageRepository.findById(id).getOrNull()
            ?: throw BusinessException("Imagem não encontrada")
        recipeBucketRepository.deleteImage(id)
        recipeImageRepository.deleteById(id)
    }
}