package com.flavor.recipes.recipe.converters

import com.fasterxml.jackson.databind.ObjectMapper
import com.flavor.recipes.recipe.entities.RecipeIngredientEntity
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class RecipeIngredientEntitySetConverter : AttributeConverter<List<RecipeIngredientEntity>, String> {
    private val objectMapper = ObjectMapper()
    override fun convertToDatabaseColumn(attribute: List<RecipeIngredientEntity>?): String {
        return objectMapper.writeValueAsString(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): List<RecipeIngredientEntity> {
        return objectMapper.readValue(
            dbData,
            objectMapper.typeFactory.constructCollectionType(List::class.java, RecipeIngredientEntity::class.java)
        )
    }
}