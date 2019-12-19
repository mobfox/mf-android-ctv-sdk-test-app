package com.market.ctvsampleapp.domain.videorepository

class GetVideoByIdUseCase(private val videoRepository: VideoRepository) {

    fun execute(videoId: Int): UiVideo {
        return mapVideoToUiVideo(videoRepository.getVideo(videoId))
    }

}