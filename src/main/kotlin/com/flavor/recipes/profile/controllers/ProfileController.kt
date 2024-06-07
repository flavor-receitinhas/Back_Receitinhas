package com.flavor.recipes.profile.controllers

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.profile.dtos.ProfileNameDto
import com.flavor.recipes.profile.entities.ProfileEntity
import com.flavor.recipes.profile.repositories.BucketRepository
import com.flavor.recipes.profile.repositories.ProfileRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*


@RestController
@RequestMapping("/profile")
class ProfileController {
    @Autowired
    lateinit var profileRepository: ProfileRepository

    @Autowired
    lateinit var bucketRepository: BucketRepository

    val LIMIT_FILE_SIZE = 3000000
    val TYPE_CONTENT_IMAGE = listOf("JPG", "GIF", "PNG", "JPEG")

    @GetMapping("/{userID}")
    @ResponseBody
    fun getProfile(authentication: Authentication, @PathVariable userID: String): ProfileEntity {
        return profileRepository.findByUserID(userID)
            ?: return createProfile(userID = userID)
    }

    @PostMapping("/{userID}")
    @ResponseBody
    fun updateProfile(
        authentication: Authentication,
        @RequestBody body: ProfileEntity,
        @PathVariable userID: String,
    ): ProfileEntity {
        if ((body.name?.isBlank() == true)) throw BusinessException("Nome não pode ser vazio")
        val find = profileRepository.findByUserID(userID)
            ?: throw BusinessException("Perfil não encontrado")
        return profileRepository.save(
            find.copy(
                biography = body.biography,
                updatedAt = Date().time,
                name = body.name,
            )
        )
    }

    @PutMapping("/{userID}/image")
    fun handleFileUpload(
        @RequestPart file: MultipartFile?,
        @PathVariable userID: String,
    ): ProfileEntity {
        var image: String? = null
        val find: ProfileEntity =
            profileRepository.findByUserID(userID) ?: throw BusinessException("Perfil não encontrado")
        if (file != null) {
            validateImage(file)
            bucketRepository.saveImage(userID, file.bytes, file.contentType!!)
            image = bucketRepository.getLinkImage(userID)
        }
        return profileRepository.save(find.copy(image = image, updatedAt = Date().time))
    }

    private fun validateImage(file: MultipartFile) {
        if (file.size > LIMIT_FILE_SIZE) throw BusinessException("Imagem maior que o permetido: 3mb")
        val typeImage = file.contentType!!.replace("image/", "").uppercase()
        if (!TYPE_CONTENT_IMAGE.contains(typeImage)) throw BusinessException("Tipo de arquivo não permitido.")
    }

    @PutMapping("/{userID}/name")
    fun updateName(
        @RequestBody profileName: ProfileNameDto,
        @PathVariable userID: String,
    ): ProfileEntity {
        val find = profileRepository.findByName(profileName.name)
        if (find != null) {
            throw BusinessException("Esse nome ja está em uso, tente outro")
        }
        val userFind = profileRepository.findByUserID(userID)
        if (userFind == null) {
            return createProfile(userID, profileName.name)
        }
        return profileRepository.save(userFind.copy(name = profileName.name, updatedAt = Date().time))
    }

    private fun createProfile(userID: String, name: String? = null): ProfileEntity {
        val profile = ProfileEntity(
            updatedAt = Date().time,
            biography = "",
            createdAt = Date().time,
            userID = userID,
            image = null,
            name = name,
        )
        return profileRepository.save(profile)
    }
}