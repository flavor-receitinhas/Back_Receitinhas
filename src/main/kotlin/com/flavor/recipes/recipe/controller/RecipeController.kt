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

@RestController
@RequestMapping("/recipe")
class RecipeController {
    @Autowired
    lateinit var recipeRepository: RecipeRepository
    @GetMapping()
    fun getIngredient(@RequestParam("id", required = false) id: Long?): Any {
        val findIngredient = id?.let { recipeRepository.findById(it) }
        if (id == null) {
            return recipeRepository.findAll();
        }
        if (findIngredient!!.isPresent) {
            return findIngredient
        }

        return "Not Found"
    }

    @PostMapping
    fun create(@RequestBody body: RecipeEntity){
        recipeRepository.save(body)
    }

}