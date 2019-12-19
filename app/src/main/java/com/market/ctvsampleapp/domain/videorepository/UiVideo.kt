package com.market.ctvsampleapp.domain.videorepository

data class UiVideo(
    val id: Int,
    val title: String,
    val description: String,
    val studio: String,
    val videoUrl: String,
    val backgroundImageUrl: String,
    val cardImageUrl: String,
    val category: String
)