package com.flavor.recipes.recipe.repositories

import com.flavor.recipes.recipe.entities.RecipeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository()
interface RecipeRepository : JpaRepository<RecipeEntity, Long> {

}