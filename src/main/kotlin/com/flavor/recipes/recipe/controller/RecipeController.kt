package com.flavor.recipes.recipe.controller

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.profile.entities.ProfileEntity
import com.flavor.recipes.recipe.dtos.RecipeCreateDto
import com.flavor.recipes.recipe.dtos.RecipeUpdateDto
import com.flavor.recipes.recipe.entities.RecipeEntity
import com.flavor.recipes.recipe.entities.RecipeImageEntity
import com.flavor.recipes.recipe.entities.RecipeStatus
import com.flavor.recipes.recipe.repositories.RecipeBucketRepository
import com.flavor.recipes.recipe.repositories.RecipeRepository
import com.flavor.recipes.recipe.services.RecipeService
import com.flavor.recipes.user.entities.UserEntity
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.util.Date

@RestController
@RequestMapping("/recipe")
@Tag(name = "Recipe")
class RecipeController {
    @Autowired
    lateinit var recipeService: RecipeService

    @GetMapping
    fun list(
        @RequestParam isDesc: Boolean?,
        @RequestParam page: Int?,
        @RequestParam sort: String?
    ): List<RecipeEntity> {
        return recipeService.findByStatusNot(
            RecipeStatus.blocked.name, isDesc = isDesc ?: true,
            page = page ?: 0,
            sort = sort ?: RecipeEntity::createdAt.name
        )
    }

    @GetMapping("/user/{userId}")
    fun findByUser(
        @PathVariable userId: String,
        @RequestParam isDesc: Boolean?,
        @RequestParam page: Int?,
        @RequestParam sort: String?
    ): List<RecipeEntity> {
        return recipeService.findByUser(
            userId = userId,
            isDesc = isDesc ?: true,
            page = page ?: 0,
            sort = sort ?: RecipeEntity::createdAt.name
        )
    }


    @GetMapping("/{id}")
    fun get(@PathVariable id: String): RecipeEntity {
        val recipe = recipeService.findById(id)
            ?: throw BusinessException("Receita não encontrada")
        if (recipe.status == RecipeStatus.blocked) {
            throw BusinessException("Está receita foi bloqueada")
        }
        return recipe
    }

    @PostMapping
    fun create(@RequestBody body: RecipeCreateDto, @AuthenticationPrincipal user: UserEntity): RecipeEntity {
        if (body.status == RecipeStatus.blocked) {
            throw BusinessException("Não pode criar uma receita bloqueada")
        }
        return recipeService.create(body, user.id)
    }

    @PutMapping("/{id}")
    fun update(@RequestBody body: RecipeUpdateDto, @PathVariable id: String): RecipeEntity {
        return recipeService.update(body, id)
    }

    @PutMapping("/{recipeId}/images")
    fun createFile(
        @RequestPart file: MultipartFile,
        @PathVariable recipeId: String,
    ) {
        return recipeService.createImage(recipeId, file)
    }

    @DeleteMapping("/{recipeId}/images/{imageId}")
    fun deleteFile(@PathVariable imageId: String) {
        return recipeService.deleteImage(imageId)
    }

    @PutMapping("/{recipeId}/thumbs")
    fun createThumbs(
        @RequestPart file: MultipartFile,
        @PathVariable recipeId: String,
    ) {
        return recipeService.createImage(recipeId, file, true)
    }

    @DeleteMapping("/{recipeId}/thumbs/{imageId}")
    fun deleteThumbs(@PathVariable imageId: String) {
        return recipeService.deleteImage(imageId)
    }

    @GetMapping("/{recipeId}/images")
    fun getImages(
        @PathVariable recipeId: String,
    ): List<RecipeImageEntity> {
        return recipeService.findAllImages(recipeId)
    }

}