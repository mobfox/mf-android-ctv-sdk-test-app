package com.market.ctvsampleapp.presentation.videodetails

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import com.market.ctvsampleapp.domain.videorepository.UiVideo

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(viewHolder: ViewHolder, item: Any) {
        val video = item as UiVideo
        viewHolder.title.text = video.title
        viewHolder.subtitle.text = video.studio
        viewHolder.body.text = video.description
    }
}