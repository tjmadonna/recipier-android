package com.inelasticcollision.recipelink.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeInfo(

        @field:Json(name = "title")
        val title: String?,

        @field:Json(name = "url")
        val url: String?,

        @field:Json(name = "mainImage")
        val mainImage: String?,

        @field:Json(name = "images")
        val images: List<String>?
)