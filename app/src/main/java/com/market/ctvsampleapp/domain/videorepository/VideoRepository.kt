package com.market.ctvsampleapp.domain.videorepository

interface VideoRepository {

    fun loadVideos(): List<Video>

    fun getVideo(videoId: Int): Video

}