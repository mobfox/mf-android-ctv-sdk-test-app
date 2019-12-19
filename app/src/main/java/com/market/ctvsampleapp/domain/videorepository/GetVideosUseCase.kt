package com.market.ctvsampleapp.domain.videorepository

import androidx.leanback.widget.HeaderItem

class GetVideosUseCase(private val videoRepository: VideoRepository) {

    fun execute(): Map<HeaderItem, List<UiVideo>> {
        val loadedVideos = videoRepository.loadVideos()
        return loadedVideos.groupBy { it.category }
            .toList()
            .mapIndexed { index, pair ->
                mapCategoryToUiCategory(index, pair) to mapVideosToUiVideos(pair)
            }.toMap()
    }

    private fun mapCategoryToUiCategory(index: Int, pair: Pair<String, List<Video>>) =
        HeaderItem(index.toLong(), pair.first)

    private fun mapVideosToUiVideos(pair: Pair<String, List<Video>>): List<UiVideo> =
        pair.second.map {
            mapVideoToUiVideo(it)
        }
}

fun mapVideoToUiVideo(it: Video): UiVideo {
    return UiVideo(
        it.id,
        it.title,
        it.description,
        it.studio,
        it.videoUrl,
        it.backgroundImageUrl,
        it.cardImageUrl,
        it.category
    )
}