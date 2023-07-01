package com.example.testsmitch.data.remote.responses

data class ImageResponse (
    val hints: List<ImageResult>,
    val total: Int,
    val totalHints: Int
)