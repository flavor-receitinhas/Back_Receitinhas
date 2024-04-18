package com.flavor.recipes.favorite.controllers

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.core.HandleException
import com.flavor.recipes.favorite.entities.Favorite
import com.flavor.recipes.favorite.repositories.FavoriteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/favorite")
class FavoriteController {
    @Autowired
    lateinit var favoriteRepository: FavoriteRepository

    @GetMapping("/{userId}")
    fun list(
        authentication: Authentication,
        @PathVariable userId: String,
        @RequestParam sort: String?,
        @RequestParam page: Int?,
    ): ResponseEntity<Any> {
        return try {
            if (userId != authentication.principal.toString()) {
                throw BusinessException("User não pode ver os favoritos de outro usuario")
            }
            val find = favoriteRepository.findByUserId(
                userId,
                PageRequest.of(
                    page ?: 0,
                    25,
                    Sort.by(sort ?: Favorite::createdAt.name)
                )
            )
            ResponseEntity.ok(find)
        } catch (e: Exception) {
            HandleException().handle(e)
        }
    }

    @PostMapping("/{userId}")
    @ResponseBody
    fun create(authentication: Authentication, @RequestBody body: Favorite): ResponseEntity<Any> {
        return try {
            if (body.userId != authentication.principal.toString()) {
                throw BusinessException("User do token é diferente do body")
            }
            val find = favoriteRepository.save(body.copy(createdAt = Date().time, updatedAt = Date().time))
            ResponseEntity.ok(find)
        } catch (e: Exception) {
            HandleException().handle(e)
        }
    }

    @DeleteMapping("/{userId}/{id}")
    @ResponseBody
    fun delete(
        authentication: Authentication,
        @PathVariable id: Long,
    ): ResponseEntity<Any> {
        return try {
            val userId = authentication.principal.toString()
            val find = favoriteRepository.findById(id)
            if (!find.isPresent) {
                return ResponseEntity.notFound().build()
            }
            if (find.get().userId != userId) {
                throw BusinessException("User não pode remover dos favoritos")
            }
            val result = favoriteRepository.deleteById(id)
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            HandleException().handle(e)
        }
    }
}