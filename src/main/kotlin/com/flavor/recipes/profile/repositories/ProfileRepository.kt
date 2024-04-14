package com.flavor.recipes.profile.repositories

import com.flavor.recipes.profile.entities.ProfileEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository : JpaRepository<ProfileEntity, Long> {
    fun findByUserID(userID: String): ProfileEntity?
    fun findByName(name: String): ProfileEntity?
}