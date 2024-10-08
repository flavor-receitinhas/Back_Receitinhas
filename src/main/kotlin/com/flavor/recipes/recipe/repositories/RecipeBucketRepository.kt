package com.flavor.recipes.recipe.repositories

import com.flavor.recipes.core.BusinessException
import com.flavor.recipes.core.buckets.BucketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.web.multipart.MultipartFile


@Repository
class RecipeBucketRepository {
    @Autowired
    lateinit var bucketService: BucketService

    companion object {
        const val LIMIT_FILE_SIZE = 6000000
        const val BUCKET_NAME = "recipe"
        val TYPE_CONTENT_IMAGE = listOf("JPG", "GIF", "PNG", "JPEG")
    }


    fun saveImage(name: String, file: MultipartFile) {
        validateImage(file)
        bucketService.saveImage(name, file, BUCKET_NAME)
    }

    fun getLinkImage(name: String): String {
        return bucketService.getLinkImage(name, BUCKET_NAME)
    }

    fun deleteImage(name: String) {
        bucketService.deleteImage(name, BUCKET_NAME)
    }

    private fun validateImage(file: MultipartFile) {
        val limit = LIMIT_FILE_SIZE
        if (file.size > limit) throw BusinessException("Imagem maior que o permitido: ${limit.toString()[0]}mb")
        val typeImage = file.contentType!!.replace("image/", "").uppercase()
        if (!TYPE_CONTENT_IMAGE.contains(typeImage)) {
            throw BusinessException("Tipo de arquivo não permitido.")
        }
    }
}