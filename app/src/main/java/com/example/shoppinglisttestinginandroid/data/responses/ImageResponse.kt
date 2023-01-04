package com.example.shoppinglisttestinginandroid.data.responses


data class ImageResponse(
    val hits: List<ImageResult>,
    val total: Int,
    val totalHits: Int
)