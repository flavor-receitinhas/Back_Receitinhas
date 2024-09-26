package com.flavor.recipes.recipe.controller

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.recipe.dtos.RecipeCreateDto
import com.flavor.recipes.recipe.dtos.RecipeUpdateDto
import com.flavor.recipes.recipe.entities.RecipeEntity
import com.flavor.recipes.recipe.entities.RecipeStatus
import com.flavor.recipes.recipe.repositories.RecipeRepository
import com.flavor.recipes.recipe.services.RecipeService
import com.flavor.recipes.user.entities.UserEntity
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
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
        return recipeService.create(
            RecipeEntity(
                details = body.details,
                difficultyRecipe = body.difficultyRecipe,
                ingredients = body.ingredients,
                instruction = body.instruction,
                portion = body.portion,
                serveFood = body.serveFood,
                subTitle = body.subTitle,
                timePrepared = body.timePrepared,
                title = body.title,
                status = body.status,
                userId = user.id,
                images = emptyList(),
                thumb = "",
                createdAt = Timestamp.from(Date().toInstant()),
                updatedAt = Timestamp.from(Date().toInstant())
            )
        )
    }

    @PutMapping("/{id}")
    fun update(@RequestBody body: RecipeUpdateDto, @PathVariable id: String): RecipeEntity {
        val recipe = recipeService.findById(id)
            ?: throw BusinessException("Receita não encontrada")
        if (recipe.status == RecipeStatus.blocked) {
            throw BusinessException("Está receita não pode ser alterada.")
        }
        return recipeService.update(
            recipe.copy(
                details = body.details,
                difficultyRecipe = body.difficultyRecipe,
                ingredients = body.ingredients,
                instruction = body.instruction,
                portion = body.portion,
                serveFood = body.serveFood,
                subTitle = body.subTitle,
                timePrepared = body.timePrepared,
                title = body.title,
                status = body.status
            )
        )
    }

}