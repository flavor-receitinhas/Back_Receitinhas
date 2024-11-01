package com.flavor.recipes.favorite.controllers

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.favorite.dtos.FavoriteCheckDto
import com.flavor.recipes.favorite.dtos.FavoriteCreateDto
import com.flavor.recipes.favorite.dtos.ListFavoriteDto
import com.flavor.recipes.favorite.entities.Favorite
import com.flavor.recipes.favorite.repositories.FavoriteRepository
import com.flavor.recipes.recipe.repositories.RecipeImageRepository
import com.flavor.recipes.recipe.repositories.RecipeRepository
import com.flavor.recipes.user.entities.UserEntity
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.sql.Timestamp
import java.util.*
import kotlin.jvm.optionals.getOrNull

@RestController
@RequestMapping("/favorite")
@Tag(name = "Favorite")
class FavoriteController {
    @Autowired
    lateinit var favoriteRepository: FavoriteRepository

    @Autowired
    lateinit var recipeRepository: RecipeRepository

    @Autowired
    lateinit var recipeImageRepository: RecipeImageRepository

    @GetMapping
    fun list(
        @AuthenticationPrincipal user: UserEntity,
        @RequestParam sort: String?,
        @RequestParam page: Int?,
        @RequestParam isDesc: Boolean = false,
        @RequestParam search: String?,
    ): List<ListFavoriteDto> {
        if (search != null && search.isEmpty()) throw BusinessException("search não pode ser vazio")
        val find = if (search != null) {
            favoriteRepository.findByUserIdAndSearch(
                user.id,
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
                user.id,
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
            val thumb = recipeImageRepository.findByRecipeIdAndThumb(it.recipeId, true)
            ListFavoriteDto(
                favorite = it.copy(name = recipe.title),
                thumb = thumb.getOrNull()?.link,
                timePrepared = recipe.timePrepared
            )
        }.toList()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@AuthenticationPrincipal user: UserEntity, @RequestBody body: FavoriteCreateDto): Favorite {
        val recipe = recipeRepository.findById(body.recipeId)
        if (!recipe.isPresent) throw BusinessException("Receita não encontrada")
        return favoriteRepository.save(
            Favorite(
                userId = user.id,
                recipeId = body.recipeId,
                name = recipe.get().title,
                createdAt = Timestamp.from(Date().toInstant()),
                updatedAt = Timestamp.from(Date().toInstant())
            )
        )
    }

    @GetMapping("/recipe/{recipeId}")
    fun checkRecipe(@AuthenticationPrincipal user: UserEntity, @PathVariable recipeId: String): FavoriteCheckDto {
        val recipe = recipeRepository.findById(recipeId)
        if (!recipe.isPresent) throw BusinessException("Receita não encontrada")
        val result = favoriteRepository.findByUserIdAndRecipeId(
            recipeId = recipeId,
            userId = user.id,
        )
        return FavoriteCheckDto(result.isPresent)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @AuthenticationPrincipal user: UserEntity,
        @PathVariable id: Long,
    ) {
        val find = favoriteRepository.findById(id)
        if (!find.isPresent) {
            throw BusinessException("Receita não encontrada")
        }
        if (find.get().userId != user.id) {
            throw BusinessException("User não pode remover dos favoritos")
        }
        return favoriteRepository.deleteById(id)
    }
}