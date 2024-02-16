package com.flavor.recipes.recipe.controller

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
import java.net.http.HttpResponse
import kotlin.concurrent.thread

@RestController
@RequestMapping("/recipe")
class RecipeController {
    @Autowired
    lateinit var recipeRepository: RecipeRepository
    @GetMapping()
    fun getIngredient(authentication: Authentication): Any {
        try {
            return recipeRepository.findAll();
        } catch (e: Exception) {
            Sentry.captureException(e)
            return ResponseEntity.badRequest()
        }
    }

    @GetMapping("/{id}")
    fun getIngredientById(@PathVariable id: String): Any {
            val findIngredient = recipeRepository.findById(id)
            if (!findIngredient.isPresent) {
                return "Not Found"
            }
            return findIngredient.get()
    }

    @PostMapping
    fun create(@RequestBody body: RecipeEntity){
        recipeRepository.save(body)
    }

}