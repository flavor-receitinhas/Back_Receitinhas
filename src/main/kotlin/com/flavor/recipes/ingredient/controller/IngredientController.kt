package com.flavor.recipes.ingredient.controller

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.ingredient.entities.CreateEntityDto
import com.flavor.recipes.ingredient.entities.IngredientEntity
import com.flavor.recipes.ingredient.repository.IngredientRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @GetMapping
    fun list(
        @RequestParam("name", required = false) name: String?,
        @RequestParam("page", required = false) page: Int?
    ): List<IngredientEntity> {
        val pagination = PageRequest.of(page ?: 0, 25)
        if (name != null) {
            return ingredientRepository.findByNameContains(name, pagination)
        }
        return ingredientRepository.findAll(pagination).content
    }

    @GetMapping("/{id}")
    fun byId(@PathVariable id: String): IngredientEntity {
        return ingredientRepository.findById(id).get()
    }

    @PostMapping
    fun add(@RequestBody ingredient: CreateEntityDto): IngredientEntity {
        return ingredientRepository.save(IngredientEntity(name = ingredient.name));
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody ingredient: CreateEntityDto): IngredientEntity {
        val find = ingredientRepository.findById(id)
        if (!find.isPresent) throw BusinessException("Not Found")
        return ingredientRepository.save(find.get().copy(name = ingredient.name))
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable("id") id: String) {
        return ingredientRepository.deleteById(id)
    }
}