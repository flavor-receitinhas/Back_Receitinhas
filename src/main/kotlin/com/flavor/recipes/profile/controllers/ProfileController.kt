package com.flavor.recipes.profile.controllers

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.core.HandleException
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

const val LIMIT_FILE_SIZE = 3000000
val TYPE_CONTENT_IMAGE = listOf("JPG", "GIF", "PNG", "JPEG")
@RestController
@RequestMapping("/profile")
class ProfileController {
    @Autowired
    lateinit var profileRepository: ProfileRepository
    @Autowired
    lateinit var bucketRepository: BucketRepository
    @GetMapping("/{userID}")
    @ResponseBody
    fun getProfile(authentication: Authentication, @PathVariable userID: String): ResponseEntity<Any> {
        try {
            var find = profileRepository.findByUserID(userID)
            if (find == null){
                find = ProfileEntity(
                    updatedAt = Date().time,
                    biography = "",
                    createdAt = Date().time,
                    userID = userID,
                    image = null,
                    name = null,
                )
                profileRepository.save(find)
            }
            return ResponseEntity.ok(find)
        }catch (e: Exception){
            return HandleException().handle(e)
        }
    }
    @PostMapping("/{userID}")
    @ResponseBody
    fun updateProfile(authentication: Authentication,
                      @RequestBody body: ProfileEntity,
                      @PathVariable userID: String
    ): ResponseEntity<Any> {
        try {
            if ((body.name?.isBlank() == true)) throw BusinessException("Nome não pode ser vazio")
            val find = profileRepository.findByUserID(userID) ?: throw BusinessException("Perfil não encontrado")
            val result = profileRepository.save(find.copy(
                biography = body.biography,
                updatedAt = Date().time,
                name = body.name,
            ))
            return ResponseEntity.ok(result)
        }catch (e: Exception){
            return HandleException().handle(e)
        }
    }

    @PutMapping("/{userID}/image")
    fun handleFileUpload(
        @RequestPart file: MultipartFile?,
        @PathVariable userID: String
    ): ResponseEntity<Any> {
        return try {
            var image: String? = null
            val find: ProfileEntity = profileRepository.findByUserID(userID) ?: throw BusinessException("Perfil não encontrado")
            if (file != null){
                validateImage(file)
                bucketRepository.saveImage(userID, file.bytes, file.contentType!!)
                image = bucketRepository.getLinkImage(userID)
            }
            val result = profileRepository.save(find.copy(image = image, updatedAt = Date().time))
            ResponseEntity.ok(result)
        } catch (e: Exception){
            HandleException().handle(e)
        }
    }
    private fun validateImage(file: MultipartFile){
        if (file.size > LIMIT_FILE_SIZE) throw BusinessException("Imagem maior que o permetido: 3mb")
        val typeImage = file.contentType!!.replace("image/", "").uppercase()
        if (!TYPE_CONTENT_IMAGE.contains(typeImage)) throw BusinessException("Tipo de arquivo não permitido.")
    }
    @PutMapping("/{userID}/name")
    fun updateName(
        @RequestBody profileName: ProfileNameDto,
        @PathVariable userID: String
    ): ResponseEntity<Any> {
        return try {
            val find = profileRepository.findByName(profileName.name)
            if (find != null){
                throw BusinessException("Esse nome ja está em uso, tente outro")
            }
            val userFind = profileRepository.findByUserID(userID)
            val result = userFind?.let { profileRepository.save(it.copy(name = profileName.name, updatedAt = Date().time)) }
            ResponseEntity.ok(result)
        } catch (e: Exception){
            HandleException().handle(e)
        }
    }
}