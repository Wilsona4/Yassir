package com.example.yassir.data.model.movie

data class MovieErrorResponse(
    val status_code: Int,
    val status_message: String,
    val success: Boolean
)