package com.flavor.recipes.recipe.controller

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.recipe.dtos.*
import com.flavor.recipes.recipe.entities.RecipeEntity
import com.flavor.recipes.recipe.entities.RecipeImageEntity
import com.flavor.recipes.recipe.entities.RecipeStatus
import com.flavor.recipes.recipe.services.RecipeService
import com.flavor.recipes.user.entities.DifficultyRecipes
import com.flavor.recipes.user.entities.UserEntity
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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
        @RequestParam sort: String?,
        @RequestParam timePreparedTo: Int?,
        @RequestParam timePreparedFrom: Int?,
        @RequestParam portionTo: Int?,
        @RequestParam portionFrom: Int?,
        @RequestParam difficultyRecipe: DifficultyRecipes?,
        @RequestParam search: String?
    ): List<RecipeListDto> {
        return recipeService.search(
            isDesc = isDesc ?: true,
            page = page ?: 0,
            sort = sort ?: RecipeEntity::createdAt.name,
            portionTo = portionTo,
            portionFrom = portionFrom,
            timePreparedTo = portionTo,
            timePreparedFrom = portionFrom,
            difficultyRecipe = difficultyRecipe,
            search = search,
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
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody body: RecipeCreateDto, @AuthenticationPrincipal user: UserEntity): RecipeEntity {
        return recipeService.create(body, user.id)
    }

    @PutMapping("/{id}")
    fun update(@RequestBody body: RecipeUpdateDto, @PathVariable id: String): RecipeEntity {
        return recipeService.update(body, id)
    }

    @PostMapping("/{recipeId}/images")
    @ResponseStatus(HttpStatus.CREATED)
    fun createFile(
        @RequestPart file: MultipartFile,
        @PathVariable recipeId: String,
    ) {
        return recipeService.createImage(recipeId, file)
    }

    @DeleteMapping("/{recipeId}/images/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteFile(@PathVariable imageId: String) {
        return recipeService.deleteImage(imageId)
    }

    @PostMapping("/{recipeId}/thumbs")
    @ResponseStatus(HttpStatus.CREATED)
    fun createThumbs(
        @RequestPart file: MultipartFile,
        @PathVariable recipeId: String,
    ) {
        return recipeService.createImage(recipeId, file, true)
    }

    @DeleteMapping("/{recipeId}/thumbs/{imageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteThumbs(@PathVariable imageId: String) {
        recipeService.deleteImage(imageId)
    }

    @GetMapping("/{recipeId}/images")
    fun getImages(
        @PathVariable recipeId: String,
    ): List<RecipeImageEntity> {
        return recipeService.findAllImages(recipeId)
    }

    @PostMapping("/{recipeId}/ingredients")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateIngredients(
        @RequestBody body: List<RecipeIngredientCreateDto>,
        @PathVariable recipeId: String,
    ) {
        for (ingredient in body) {
            recipeService.createRecipeIngredient(ingredient, recipeId)
        }
    }

    @GetMapping("/{recipeId}/ingredients")
    @ResponseStatus(HttpStatus.OK)
    fun findIngredients(@PathVariable recipeId: String): List<RecipeIngredientListDto> {
        return recipeService.findIngredients(recipeId)
    }

    @DeleteMapping("/{recipeId}/ingredients/{ingredientId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteIngredients(
        @RequestBody body: List<RecipeIngredientCreateDto>,
        @PathVariable recipeId: String,
        @PathVariable ingredientId: String,
    ) {
        recipeService.deleteIngredient(ingredientId)
    }
}