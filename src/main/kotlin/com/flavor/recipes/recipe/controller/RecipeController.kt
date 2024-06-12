package com.flavor.recipes.recipe.controller

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.recipe.dtos.RecipeCreateDto
import com.flavor.recipes.recipe.dtos.RecipeUpdateDto
import com.flavor.recipes.recipe.entities.RecipeEntity
import com.flavor.recipes.recipe.entities.RecipeStatus
import com.flavor.recipes.recipe.repositories.RecipeRepository
import com.flavor.recipes.user.entities.UserEntity
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping

@RestController
@RequestMapping("/recipe")
@Tag(name = "Recipe")
class RecipeController {
    @Autowired
    lateinit var recipeRepository: RecipeRepository

    @GetMapping()
    fun list(authentication: Authentication): List<RecipeEntity> {
        return recipeRepository.findByStatusNot(RecipeStatus.blocked.name)
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): RecipeEntity {
        val recipe = recipeRepository.findById(id)
        if (!recipe.isPresent) {
            throw BusinessException("Receita não encontrada")
        }
        if (recipe.get().status == RecipeStatus.blocked) {
            throw BusinessException("Está receita foi bloqueada")
        }
        return recipe.get()
    }

    @PostMapping
    fun create(@RequestBody body: RecipeCreateDto, @AuthenticationPrincipal user: UserEntity): RecipeEntity {
        if (body.status == RecipeStatus.blocked) {
            throw BusinessException("Não pode criar uma receita bloqueada")
        }
        return recipeRepository.save(
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
                thumb = ""
            )
        )
    }

    @PutMapping("/{id}")
    fun update(@RequestBody body: RecipeUpdateDto, @PathVariable id: String): RecipeEntity {
        val recipe = recipeRepository.findById(id)
        if (!recipe.isPresent) throw BusinessException("Receita não encontrada")
        if (recipe.get().status == RecipeStatus.blocked) {
            throw BusinessException("Está receita não pode ser alterada.")
        }
        return recipeRepository.save(
            recipe.get().copy(
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