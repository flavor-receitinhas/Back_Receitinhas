package com.flavor.recipes.favorite.controllers

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.favorite.dtos.ListFavoriteDto
import com.flavor.recipes.favorite.entities.Favorite
import com.flavor.recipes.favorite.repositories.FavoriteRepository
import com.flavor.recipes.recipe.repositories.RecipeRepository
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/favorite")
@Tag(name = "Favorite")
class FavoriteController {
    @Autowired
    lateinit var favoriteRepository: FavoriteRepository

    @Autowired
    lateinit var recipeRepository: RecipeRepository

    @GetMapping("/{userId}")
    fun list(
        authentication: Authentication,
        @PathVariable userId: String,
        @RequestParam sort: String?,
        @RequestParam page: Int?,
        @RequestParam isDesc: Boolean = false,
        @RequestParam search: String?,
    ): List<ListFavoriteDto> {
        if (userId != authentication.principal.toString()) {
            throw BusinessException("User não pode ver os favoritos de outro usuario")
        }
        if (search != null && search.isEmpty()) throw BusinessException("search não pode ser vazio")
        val find = if (search != null) {
            favoriteRepository.findByUserIdAndSearch(
                userId,
                search,
                PageRequest.of(
                    page ?: 0,
                    25,
                    if (isDesc) Sort.by(sort ?: Favorite::createdAt.name).descending() else
                        Sort.by(sort ?: Favorite::createdAt.name)
                )
            )
        } else {
            favoriteRepository.findByUserId(
                userId,
                PageRequest.of(
                    page ?: 0,
                    25,
                    if (isDesc) Sort.by(sort ?: Favorite::createdAt.name).descending() else
                        Sort.by(sort ?: Favorite::createdAt.name)
                )
            )
        }
        return find.map {
            val recipe = recipeRepository.findById(it.recipeId).get()
            ListFavoriteDto(favorite = it, thumb = recipe.thumb, timePrepared = recipe.timePrepared)
        }.toList()
    }

    @PostMapping("/{userId}")
    @ResponseBody
    fun create(authentication: Authentication, @RequestBody body: Favorite): Favorite {
        if (body.userId != authentication.principal.toString()) {
            throw BusinessException("User do token é diferente do body")
        }
        val recipe = recipeRepository.findById(body.recipeId)
        if (!recipe.isPresent) throw BusinessException("Receita não encontrada")
        return favoriteRepository.save(body.copy(createdAt = Date().time, updatedAt = Date().time))
    }

    @DeleteMapping("/{userId}/{id}")
    @ResponseBody
    fun delete(
        authentication: Authentication,
        @PathVariable id: Long,
    ) {
        val userId = authentication.principal.toString()
        val find = favoriteRepository.findById(id)
        if (!find.isPresent) {
            throw BusinessException("Receita não encontrada")
        }
        if (find.get().userId != userId) {
            throw BusinessException("User não pode remover dos favoritos")
        }
        return favoriteRepository.deleteById(id)
    }
}