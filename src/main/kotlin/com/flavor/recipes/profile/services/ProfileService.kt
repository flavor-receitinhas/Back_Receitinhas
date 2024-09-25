package com.flavor.recipes.profile.services

import com.flavor.recipes.profile.entities.ProfileEntity
import com.flavor.recipes.profile.repositories.ProfileRepository
import com.flavor.recipes.recipe.services.RecipeService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProfileService {
    @Autowired
    lateinit var profileRepository: ProfileRepository

    @Autowired
    lateinit var recipeService: RecipeService

    fun byId(userId: String, name: String? = null): ProfileEntity {
        val recipes = recipeService.countByUser(userId)
        return profileRepository.findByUserID(userId)
            ?: return createProfile(userId = userId).copy(totalRecipes = recipes)
    }


    fun findByName(name: String): ProfileEntity? {
        return profileRepository.findByName(name)
    }

    fun save(profile: ProfileEntity): ProfileEntity {
        return profileRepository.save(
            profile.copy(
                updatedAt = Date().time
            )
        )
    }

    private fun createProfile(userId: String, name: String? = null): ProfileEntity {
        val profile = ProfileEntity(
            updatedAt = Date().time,
            biography = "",
            createdAt = Date().time,
            userId = userId,
            image = null,
            name = name,
        )
        return profileRepository.save(profile)
    }
}