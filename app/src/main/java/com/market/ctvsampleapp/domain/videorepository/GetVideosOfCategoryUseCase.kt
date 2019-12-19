package com.market.ctvsampleapp.domain.videorepository

class GetVideosOfCategoryUseCase(private val videoRepository: VideoRepository) {

    fun execute(category: String): List<UiVideo> {
        val loadedVideos = videoRepository.loadVideos()
        return loadedVideos
            .filter { it.category == category }
            .map { mapVideoToUiVideo(it) }
    }
}