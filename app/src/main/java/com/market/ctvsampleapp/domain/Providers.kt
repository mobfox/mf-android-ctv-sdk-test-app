package com.market.ctvsampleapp.domain

import com.market.ctvsampleapp.data.videorepository.VideoRepositoryImpl
import com.market.ctvsampleapp.domain.videorepository.GetVideoByIdUseCase
import com.market.ctvsampleapp.domain.videorepository.GetVideosOfCategoryUseCase
import com.market.ctvsampleapp.domain.videorepository.GetVideosUseCase
import com.market.ctvsampleapp.domain.videorepository.VideoRepository
import com.market.ctvsampleapp.presentation.GlideImageLoader

private var videoRepository: VideoRepository? = null
fun getVideoRepository(): VideoRepository = when (videoRepository) {
    null -> {
        videoRepository = VideoRepositoryImpl()
        videoRepository!!
    }
    else -> videoRepository!!
}


fun getVideosUseCaseProvider(): GetVideosUseCase {
    return GetVideosUseCase(getVideoRepository())
}

fun getVideosOfCategoryUseCaseProvider(): GetVideosOfCategoryUseCase {
    return GetVideosOfCategoryUseCase(getVideoRepository())
}

fun getVideoByIdUseCaseProvider(): GetVideoByIdUseCase {
    return GetVideoByIdUseCase(getVideoRepository())
}

fun getImageLoader(): GlideImageLoader {
    return GlideImageLoader()
}