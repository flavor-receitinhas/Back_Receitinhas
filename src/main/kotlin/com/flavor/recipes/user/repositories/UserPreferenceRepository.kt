package com.flavor.recipes.user.repositories

import com.flavor.recipes.user.entities.UserPreference
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserPreferenceRepository: JpaRepository<UserPreference, Long> {
    fun findByUserId(userId: String): UserPreference?
}