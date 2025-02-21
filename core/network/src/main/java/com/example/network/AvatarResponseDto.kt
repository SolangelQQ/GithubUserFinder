package com.example.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AvatarResponseDto(
    @Json(name = "login")
    val login: String,
    @Json(name = "avatar_url")
    val url: String
)