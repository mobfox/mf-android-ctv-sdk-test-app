package com.market.ctvsampleapp.presentation

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.market.ctvsampleapp.R
import com.market.ctvsampleapp.domain.getImageLoader
import com.market.ctvsampleapp.domain.videorepository.UiVideo
import kotlin.properties.Delegates

private const val CARD_WIDTH = 313
private const val CARD_HEIGHT = 176

class CardPresenter : Presenter() {
    private var defaultCardImage: Drawable? = null
    private var selectedBackgroundColor: Int by Delegates.notNull()
    private var defaultBackgroundColor: Int by Delegates.notNull()
    private var imageLoader: ImageLoader = getImageLoader()

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        defaultBackgroundColor = ContextCompat.getColor(
            parent.context,
            R.color.default_background
        )
        selectedBackgroundColor =
            ContextCompat.getColor(
                parent.context,
                R.color.selected_background
            )
        defaultCardImage = ContextCompat.getDrawable(
            parent.context,
            R.drawable.video
        )

        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        val video = item as UiVideo
        val cardView = viewHolder.view as ImageCardView

        cardView.titleText = video.title
        cardView.contentText = video.studio
        cardView.setMainImageDimensions(
            CARD_WIDTH,
            CARD_HEIGHT
        )

        imageLoader.loadImage(
            viewHolder.view.context,
            video.cardImageUrl,
            defaultCardImage,
            cardView.mainImageView
        )
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        // Remove references to images so that the garbage collector can free up memory
        cardView.badgeImage = null
        cardView.mainImage = null
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) selectedBackgroundColor else defaultBackgroundColor
        // Both background colors should be set because the view's background is temporarily visible
        // during animations.
        view.setBackgroundColor(color)
        view.setInfoAreaBackgroundColor(color)
    }
}
