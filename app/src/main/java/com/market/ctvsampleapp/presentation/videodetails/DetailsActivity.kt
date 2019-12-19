package com.market.ctvsampleapp.presentation.videodetails

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentActivity
import com.market.ctvsampleapp.R

class DetailsActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
    }

    companion object {
        const val SHARED_ELEMENT_NAME = "hero"
        const val VIDEO_ID = "VIDEO_ID"

        fun getActivityStartBundle(activity: Activity, viewToTransitionFrom: View) =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                viewToTransitionFrom,
                SHARED_ELEMENT_NAME
            ).toBundle()

        fun getActivityStartIntent(activity: Activity, videoId: Int): Intent {
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra(VIDEO_ID, videoId)
            return intent
        }
    }
}
