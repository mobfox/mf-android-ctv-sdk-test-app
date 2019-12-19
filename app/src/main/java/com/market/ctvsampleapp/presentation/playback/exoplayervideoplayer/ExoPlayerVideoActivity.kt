package com.market.ctvsampleapp.presentation.playback.exoplayervideoplayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.market.ctvsampleapp.R
import com.market.ctvsampleapp.domain.getVideoByIdUseCaseProvider
import com.market.ctvsampleapp.domain.videorepository.UiVideo
import com.market.ctvsampleapp.presentation.videodetails.DetailsActivity
import com.market.ctv.CtvSdk
import com.market.ctv.AdCallbacksListener
import kotlinx.android.synthetic.main.activity_basic_video.*

private const val TAG = "ExoPlayerVideoActivity"
private const val VIDEO_ID = "VIDEO_ID"
private const val VIDEO_NOT_LOADED = -1

class ExoPlayerVideoActivity : AppCompatActivity() {

    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private lateinit var video: UiVideo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val videoId = intent.getIntExtra(DetailsActivity.VIDEO_ID, VIDEO_NOT_LOADED)

        if (videoId != VIDEO_NOT_LOADED) {
            video = getVideoByIdUseCaseProvider().execute(videoId)

            setContentView(R.layout.activity_basic_video)

            playButton.setOnClickListener {
                playVideo()
            }

            playVideo()
        } else {
            Log.e(TAG, "Video ID not set, finishing Activity")
            finish()
        }
    }

    fun resumePreviousStateOfPlayback() {
        playButton.visibility = View.INVISIBLE
        initializePlayer()
    }

    private fun playVideo() {
        playButton.visibility = View.GONE
        resumePreviousStateOfPlayback()
        CtvSdk.getInstance().playAds(
            this,
            { player?.currentPosition },
            { player?.duration },
            object : AdCallbacksListener {

                override fun onAdStart() {
                    releasePlayer()
                }

                override fun onAdStopped(hasVideoEnded: Boolean) {
                    resumePreviousStateOfPlayback()
                }
            })
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (event?.keyCode) {
            KeyEvent.KEYCODE_DPAD_CENTER -> playerView.showController()
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun initializePlayer() {
        player = ExoPlayerFactory.newSimpleInstance(this)
        playerView.player = player

        val dataSourceFactory = DefaultDataSourceFactory(
            this,
            Util.getUserAgent(this, getString(R.string.app_name))
        )
        // This is the MediaSource representing the media to be played.
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(video.videoUrl))

        player?.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)

                when (playbackState) {
                    Player.STATE_ENDED -> {
                        CtvSdk.getInstance().onVideoCompleted()
                    }
                }
            }
        })

        player!!.playWhenReady = playWhenReady
        player!!.seekTo(currentWindow, playbackPosition)
        player!!.prepare(mediaSource, false, false)
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player?.playWhenReady ?: false
            playbackPosition = player?.currentPosition ?: 0
            currentWindow = player?.currentWindowIndex ?: 0
            player?.release()
            player = null
        }
    }

    companion object {
        fun getActivityStartIntent(activity: FragmentActivity, videoId: Int): Intent {
            val intent = Intent(activity, ExoPlayerVideoActivity::class.java)
            intent.putExtra(VIDEO_ID, videoId)
            return intent
        }
    }
}