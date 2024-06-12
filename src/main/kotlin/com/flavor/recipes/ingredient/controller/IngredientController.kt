package com.flavor.recipes.ingredient.controller

import com.flavor.recipes.ingredient.entities.IngredientEntity
import com.flavor.recipes.ingredient.repository.IngredientRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/ingredient")
@Tag(name = "Ingredient")
class IngredientController(@Autowired private val ingredientRepository: IngredientRepository) {

    @GetMapping()
    fun getIngredient(@RequestParam("id", required = false) id: Long?): Any {
        val findIngredient = id?.let { ingredientRepository.findById(it) }
        if (id == null) {
            return ingredientRepository.findAll();
        }
        if (findIngredient!!.isPresent) {
            return findIngredient
        }

        return "Not Found"
    }

    @PostMapping
    fun addIngredient(@RequestBody ingredientEntity: IngredientEntity): IngredientEntity {
        return ingredientRepository.save(ingredientEntity);
    }

    @PutMapping
    fun updateIngredient() {

    }

    @DeleteMapping
    fun deleteIngredient(@RequestParam("id") id: Long): String {
        if (getIngredient(id) == "Not Found") {
            return "Não encontrado"
        }
        ingredientRepository.deleteById(id)
        return "Deletado com sucesso"
    }
}