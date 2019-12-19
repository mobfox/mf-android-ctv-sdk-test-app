package com.market.ctvsampleapp.data.videorepository

import com.market.ctvsampleapp.domain.videorepository.Video
import com.market.ctvsampleapp.domain.videorepository.VideoRepository

class VideoRepositoryImpl : VideoRepository {

    override fun loadVideos(): List<Video> {
        return listOf(
            Video(
                1,
                "Google Demo Slam",
                "Don't text and skate",
                "Google",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search.mp4",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/bg.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/Demo%20Slam/Google%20Demo%20Slam_%2020ft%20Search/card.jpg",
                "Demo Videos"
            ),
            Video(
                2,
                "Gmail Blue",
                "The future of Email",
                "Google",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue.mp4",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue/bg.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Gmail%20Blue/card.jpg",
                "Demo Videos"
            ),
            Video(
                3,
                "Google Fiber",
                "Strawberries are packed with fiber",
                "Google",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/bg.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/bg.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Fiber%20to%20the%20Pole/card.jpg",
                "Demo Videos"
            ),
            Video(
                4,
                "Google Nose",
                "Smelling is believing",
                "Google",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose.mp4",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/bg.jpg",
                "https://commondatastorage.googleapis.com/android-tv/Sample%20videos/April%20Fool's%202013/Introducing%20Google%20Nose/card.jpg",
                "Demo Videos"
            )
        )
    }

    override fun getVideo(videoId: Int): Video {
        return loadVideos().first { it.id == videoId }
    }
}