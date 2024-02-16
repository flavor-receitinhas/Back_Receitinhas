package com.flavor.recipes.recipe.controller

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.core.HandleException
import com.flavor.recipes.recipe.dtos.RecipeCreateDto
import com.flavor.recipes.recipe.entities.RecipeEntity
import com.flavor.recipes.recipe.repositories.RecipeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import io.sentry.Sentry
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import java.net.http.HttpResponse
import kotlin.concurrent.thread

@RestController
@RequestMapping("/recipe")
class RecipeController {
    @Autowired
    lateinit var recipeRepository: RecipeRepository
    @GetMapping()
    fun list(authentication: Authentication): ResponseEntity<List<RecipeEntity>> {
        try {
            return ResponseEntity.ok(recipeRepository.findAll());
        } catch (e: Exception) {
            return HandleException<List<RecipeEntity>>().handle(e)
        }
    }

    @GetMapping("/{id}")
    fun get(@PathVariable id: String): ResponseEntity<RecipeEntity> {
        try {
            val findIngredient = recipeRepository.findById(id)
            if (!findIngredient.isPresent) {
               throw BusinessException(message = "Receita não encontrada")
            }
            return ResponseEntity.ok(findIngredient.get())
        } catch (e: Exception) {
            return HandleException<RecipeEntity>().handle(e)
        }
    }

    @PostMapping
    fun create(@RequestBody body: RecipeEntity): ResponseEntity<RecipeEntity> {
        try {
            val result = recipeRepository.save(body)
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return HandleException<RecipeEntity>().handle(e)
        }
    }

    @PutMapping("/{id}")
    fun update(@RequestBody body: RecipeEntity, @PathVariable id: String): ResponseEntity<RecipeEntity> {
        try {
            if (id != body.id) throw BusinessException("Id path e Id do body diferentes")
            
            val recipe = recipeRepository.findById(id)
            if (!recipe.isPresent) throw BusinessException(message = "Receita não encontrada")

            val result = recipeRepository.save(recipe.get().copy(
                details = body.details,
                difficultyRecipe = body.difficultyRecipe,
                images = body.images,
                ingredients = body.ingredients,
                instruction = body.instruction,
                portion = body.portion,
                serveFood = body.serveFood,
                subTitle = body.subTitle,
                timePrepared = body.timePrepared,
                title = body.title,
            ))
            return ResponseEntity.ok(result)
        } catch (e: Exception) {
            return HandleException<RecipeEntity>().handle(e)
        }
    }

}