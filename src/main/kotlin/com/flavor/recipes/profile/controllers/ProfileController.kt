package com.flavor.recipes.profile.controllers

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.profile.dtos.ProfileNameDto
import com.flavor.recipes.profile.entities.ProfileEntity
import com.flavor.recipes.profile.entities.UpdateProfileDto
import com.flavor.recipes.profile.repositories.ProfileBucketRepository
import com.flavor.recipes.profile.services.ProfileService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.sql.Timestamp
import java.util.*


@RestController
@RequestMapping("/profile")
@Tag(name = "Profile")
class ProfileController {


    @Autowired
    lateinit var profileService: ProfileService

    @Autowired
    lateinit var bucketRepository: ProfileBucketRepository


    @GetMapping("/{userId}")
    @ResponseBody
    fun getProfile(@PathVariable userId: String): ProfileEntity {
        return profileService.byId(userId)
    }

    @PostMapping("/{userId}")
    fun updateProfile(
        @RequestBody body: UpdateProfileDto,
        @PathVariable userId: String,
    ): ProfileEntity {
        if ((body.name?.isBlank() == true)) throw BusinessException("Nome não pode ser vazio")
        val find = profileService.byId(userId, body.name)
        return profileService.save(
            find.copy(
                biography = body.biography,
                name = body.name,
            )
        )
    }

    @PutMapping("/{userId}/image")
    fun handleFileUpload(
        @RequestPart file: MultipartFile?,
        @PathVariable userId: String,
    ): ProfileEntity {
        var image: String? = null
        val find = profileService.byId(userId)
        if (file != null) {
            bucketRepository.saveImage(userId, file)
            image = bucketRepository.getLinkImage(userId)
        }
        return profileService.save(find.copy(image = image, updatedAt = Timestamp.from(Date().toInstant())))
    }


    @PutMapping("/{userId}/name")
    fun updateName(
        @RequestBody profileName: ProfileNameDto,
        @PathVariable userId: String,
    ): ProfileEntity {
        val find = profileService.findByName(profileName.name)
        if (find != null) {
            throw BusinessException("Esse nome ja está em uso, tente outro")
        }
        val userFind = profileService.byId(userId, profileName.name)
        return profileService.save(
            userFind.copy(
                name = profileName.name,
                updatedAt = Timestamp.from(Date().toInstant())
            )
        )
    }


}