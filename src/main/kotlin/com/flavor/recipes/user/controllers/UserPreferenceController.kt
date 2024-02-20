package com.flavor.recipes.user.controllers

import com.flavor.recipes.core.HandleException
import com.flavor.recipes.user.entities.UserPreference
import com.flavor.recipes.user.repositories.UserPreferenceRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.http.HttpRequest

@RestController
@RequestMapping("/v1/user")
class UserPreferenceController {
    @Autowired
    lateinit var repository: UserPreferenceRepository
    @GetMapping("/{userId}/preference")
    fun getPreference(@PathVariable userId: String): ResponseEntity<Any>{
        try {
            var result = repository.findByUserId(userId)
            if (result == null){
               result = UserPreference(
                   protein = listOf(),
                   userId = userId,
                   difficultyRecipe = listOf(),
                   dietaryRestriction = listOf(),
                   id = null
               )
            }
            return ResponseEntity.ok(result)
        }catch (e: Exception){
            return HandleException().handle(e)
        }
    }

    @PutMapping("/{userId}/preference")
    fun updatePreference(@PathVariable userId: String,
                         @RequestBody body: UserPreference): ResponseEntity<Any>
    {
        try {
            val find = repository.findByUserId(userId)
            val result = if (find == null){
                repository.save(body)
            }else{
                repository.save(find.copy(
                    dietaryRestriction = body.dietaryRestriction,
                    difficultyRecipe = body.difficultyRecipe,
                    protein = body.protein
                ))
            }
            return ResponseEntity.ok(result)
        }catch (e: Exception){
            return HandleException().handle(e)
        }
    }
}