package com.market.ctvsampleapp.presentation.home

import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import com.market.ctvsampleapp.R
import com.market.ctvsampleapp.domain.getVideosUseCaseProvider
import com.market.ctvsampleapp.domain.videorepository.UiVideo
import com.market.ctvsampleapp.presentation.CardPresenter
import com.market.ctvsampleapp.presentation.videodetails.DetailsActivity

private const val TAG = "MainFragment"

class MainFragment : BrowseSupportFragment() {

    private lateinit var backgroundManager: BackgroundManager

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        backgroundManager = BackgroundManager.getInstance(activity)
        backgroundManager.attach(requireActivity().window)
        setupBackground()

        setupUIElements()
        loadRows()
        setupEventListeners()
    }

    override fun onStart() {
        super.onStart()
        setupBackground()
    }

    private fun setupUIElements() {
        title = getString(R.string.browse_title)

        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        brandColor = ContextCompat.getColor(requireActivity(), R.color.brand_color)
    }

    private fun setupBackground() {
        val defaultBackground = ContextCompat.getDrawable(requireContext(), R.drawable.default_background)
        backgroundManager.drawable = defaultBackground
    }

    private fun loadRows() {
        val categoriesAndVideos = getVideosUseCaseProvider().execute()
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        val cardPresenter = CardPresenter()

        categoriesAndVideos.forEach {
            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
            // Add columns (Categories)
            rowsAdapter.add(ListRow(it.key, listRowAdapter))
            // Add row items (Videos)
            it.value.forEach { video ->
                listRowAdapter.add(video)
            }
        }
        adapter = rowsAdapter
    }

    private fun setupEventListeners() {
        onItemViewClickedListener = ItemViewClickedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            val activity = requireActivity()
            when (item) {
                is UiVideo -> {
                    val intent = DetailsActivity.getActivityStartIntent(requireActivity(), item.id)
                    val bundle = DetailsActivity.getActivityStartBundle(requireActivity(), (itemViewHolder.view as ImageCardView).mainImageView)
                    activity.startActivity(intent, bundle)
                }
                else -> {
                    Log.d(TAG, "Unknown item type: $item")
                }
            }
        }
    }
}
