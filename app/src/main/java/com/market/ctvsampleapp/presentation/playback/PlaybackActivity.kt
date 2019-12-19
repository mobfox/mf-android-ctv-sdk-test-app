package com.market.ctvsampleapp.presentation.playback

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

private const val VIDEO_ID = "VIDEO_ID"

class PlaybackActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, PlaybackVideoFragment())
                .commit()
        }
    }

    companion object {
        fun getActivityStartIntent(activity: FragmentActivity, videoId: Int): Intent {
            val intent = Intent(activity, PlaybackActivity::class.java)
            intent.putExtra(VIDEO_ID, videoId)
            return intent
        }
    }
}