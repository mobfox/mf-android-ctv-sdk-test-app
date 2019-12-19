package com.market.ctvsampleapp.presentation.playback

import android.net.Uri
import android.os.Bundle
import androidx.leanback.app.VideoSupportFragment
import androidx.leanback.app.VideoSupportFragmentGlueHost
import androidx.leanback.media.MediaPlayerAdapter
import androidx.leanback.media.PlaybackGlue
import androidx.leanback.media.PlaybackTransportControlGlue
import androidx.leanback.widget.PlaybackControlsRow
import com.market.ctvsampleapp.domain.getVideoByIdUseCaseProvider
import com.market.ctvsampleapp.presentation.videodetails.DetailsActivity
import com.market.ctv.AdCallbacksListener
import com.market.ctv.CtvSdk

class PlaybackVideoFragment : VideoSupportFragment() {

    private lateinit var transportControlGlue: PlaybackTransportControlGlue<MediaPlayerAdapter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoId = activity?.intent?.getIntExtra(DetailsActivity.VIDEO_ID, 0)
        val video = getVideoByIdUseCaseProvider().execute(videoId!!)

        val glueHost = VideoSupportFragmentGlueHost(this@PlaybackVideoFragment)
        val playerAdapter = MediaPlayerAdapter(activity)
        playerAdapter.setRepeatAction(PlaybackControlsRow.RepeatAction.INDEX_NONE)

        transportControlGlue = PlaybackTransportControlGlue(activity, playerAdapter)
        transportControlGlue.host = glueHost
        transportControlGlue.title = video.title
        transportControlGlue.subtitle = video.description
        transportControlGlue.playWhenPrepared()

//        initAds()
        playAds(transportControlGlue)
        playerAdapter.setDataSource(Uri.parse(video.videoUrl))
    }

    private fun playAds(transportControlGlue: PlaybackTransportControlGlue<MediaPlayerAdapter>) {
        val ctvSdk = CtvSdk.getInstance()
        ctvSdk.playAdsWithPlayer(this, transportControlGlue)
    }

    private fun initAds() {
        val ctvSdk = CtvSdk.getInstance()
        ctvSdk.playAds(this,
            { transportControlGlue.currentPosition },
            { transportControlGlue.duration },
            object : AdCallbacksListener {

                override fun onAdStart() {
                    transportControlGlue.pause()
                }

                override fun onAdStopped(hasVideoEnded: Boolean) {
                    if (!hasVideoEnded) {
                        transportControlGlue.play()
                    }
                }
            })

        transportControlGlue.addPlayerCallback(object : PlaybackGlue.PlayerCallback() {
            override fun onPlayCompleted(glue: PlaybackGlue?) {
                super.onPlayCompleted(glue)
                ctvSdk.onVideoCompleted()
            }
        })
    }

    override fun onPause() {
        super.onPause()
        transportControlGlue.pause()
    }
}