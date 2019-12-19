package com.market.ctvsampleapp.presentation

import android.app.Application
import com.market.ctv.Configuration
import com.market.ctv.CtvSdk

class AndroidTvSampleApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        CtvSdk.init(
            Configuration.Builder(this)
                .withPublisherId("80187188f458cfde788d961b6882fd53")
                .build()
        )
    }

}