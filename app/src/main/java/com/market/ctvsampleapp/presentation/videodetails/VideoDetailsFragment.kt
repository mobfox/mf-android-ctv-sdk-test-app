package com.market.ctvsampleapp.presentation.videodetails

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.app.DetailsSupportFragmentBackgroundController
import androidx.leanback.widget.*
import com.market.ctvsampleapp.domain.getImageLoader
import com.market.ctvsampleapp.domain.getVideoByIdUseCaseProvider
import com.market.ctvsampleapp.domain.getVideosOfCategoryUseCaseProvider
import com.market.ctvsampleapp.domain.videorepository.UiVideo
import com.market.ctvsampleapp.presentation.CardPresenter
import com.market.ctvsampleapp.presentation.ImageLoader
import com.market.ctvsampleapp.presentation.playback.PlaybackActivity
import com.market.ctvsampleapp.presentation.playback.exoplayervideoplayer.ExoPlayerVideoActivity
import com.market.ctv.CtvSdk
import com.market.ctvsampleapp.R
import kotlin.math.roundToInt

private const val TAG = "VideoDetailsFragment"
private const val ACTION_PLAY_VIDEO = 1L
private const val ACTION_PLAY_USING_EXOPLAYER = 2L
private const val DETAIL_THUMB_WIDTH = 274
private const val DETAIL_THUMB_HEIGHT = 274
private const val VIDEO_NOT_LOADED = -1

class VideoDetailsFragment : DetailsSupportFragment() {

    private var videoId: Int = VIDEO_NOT_LOADED
    private lateinit var video: UiVideo

    private lateinit var detailsBackground: DetailsSupportFragmentBackgroundController
    private lateinit var presenterSelector: ClassPresenterSelector
    private lateinit var arrayObjectAdapter: ArrayObjectAdapter
    private val imageLoader: ImageLoader = getImageLoader()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = requireActivity()
        detailsBackground = DetailsSupportFragmentBackgroundController(this)
        videoId = activity.intent.getIntExtra(DetailsActivity.VIDEO_ID, VIDEO_NOT_LOADED)

        if (videoId != VIDEO_NOT_LOADED) {
            video = getVideoByIdUseCaseProvider().execute(videoId)
            presenterSelector = ClassPresenterSelector()
            arrayObjectAdapter = ArrayObjectAdapter(presenterSelector)
            setupDetailsOverviewRow()
            setupDetailsOverviewRowPresenter()
            setupRelatedMovieListRow()
            adapter = arrayObjectAdapter
            initializeBackground(video)
            onItemViewClickedListener = ItemViewClickedListener()
        } else {
            Log.e(TAG, "Video ID not set, finishing Activity")
            requireActivity().finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Load the ads every time the Fragment is visited, so they are ready to be played whenever the Play option is pressed
        CtvSdk.getInstance().loadAds()
    }

    private fun initializeBackground(video: UiVideo) {
        detailsBackground.enableParallax()

        imageLoader.loadBitmapImage(
            requireContext(),
            video.backgroundImageUrl,
            R.drawable.default_background
        ) {
            detailsBackground.coverBitmap = it
            arrayObjectAdapter.notifyArrayItemRangeChanged(0, arrayObjectAdapter.size())
        }
    }

    private fun setupDetailsOverviewRow() {
        val activity = requireActivity()
        val row = DetailsOverviewRow(video)
        row.imageDrawable = ContextCompat.getDrawable(
            activity,
            R.drawable.default_background
        )
        val width = convertDpToPixel(
            activity,
            DETAIL_THUMB_WIDTH
        )
        val height = convertDpToPixel(
            activity,
            DETAIL_THUMB_HEIGHT
        )

        imageLoader.loadImage(
            requireContext(),
            video.cardImageUrl,
            width,
            height,
            R.drawable.default_background
        ) {
            row.imageDrawable = it
            arrayObjectAdapter.notifyArrayItemRangeChanged(0, arrayObjectAdapter.size())
        }

        val actionAdapter = ArrayObjectAdapter()

        actionAdapter.add(
            Action(
                ACTION_PLAY_VIDEO,
                resources.getString(R.string.play_video),
                resources.getString(R.string.using_leanback)
            )
        )
        actionAdapter.add(
            Action(
                ACTION_PLAY_USING_EXOPLAYER,
                resources.getString(R.string.play_video),
                resources.getString(R.string.using_exoplayer)
            )
        )
        row.actionsAdapter = actionAdapter

        arrayObjectAdapter.add(row)
    }

    private fun setupDetailsOverviewRowPresenter() {
        // Set detail background.
        val activity = requireActivity()
        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())
        detailsPresenter.backgroundColor =
            ContextCompat.getColor(activity, R.color.selected_background)

        // Hook up transition element.
        val sharedElementHelper = FullWidthDetailsOverviewSharedElementHelper()
        sharedElementHelper.setSharedElementEnterTransition(
            activity,
            DetailsActivity.SHARED_ELEMENT_NAME
        )
        detailsPresenter.setListener(sharedElementHelper)
        detailsPresenter.isParticipatingEntranceTransition = true

        detailsPresenter.onActionClickedListener = OnActionClickedListener { action ->
            val intent = when (action.id) {
                ACTION_PLAY_VIDEO -> PlaybackActivity.getActivityStartIntent(requireActivity(), videoId)
                ACTION_PLAY_USING_EXOPLAYER -> ExoPlayerVideoActivity.getActivityStartIntent(requireActivity(), videoId)
                else -> throw Exception("Unknown action id: ${action.id}")
            }
            startActivity(intent)
        }
        presenterSelector.addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
    }

    private fun setupRelatedMovieListRow() {
        val subcategories = arrayOf(getString(R.string.related_videos))
        val list = getVideosOfCategoryUseCaseProvider().execute(video.category)

        val listRowAdapter = ArrayObjectAdapter(CardPresenter())
        list.forEach { listRowAdapter.add(it) }

        val header = HeaderItem(0, subcategories[0])
        arrayObjectAdapter.add(ListRow(header, listRowAdapter))
        presenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())
    }

    private fun convertDpToPixel(context: Context, dp: Int): Int {
        val density = context.applicationContext.resources.displayMetrics.density
        return (dp.toFloat() * density).roundToInt()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {
            when (item) {
                is UiVideo -> {
                    val activity = requireActivity()
                    val intent = DetailsActivity.getActivityStartIntent(activity, item.id)
                    val bundle = DetailsActivity.getActivityStartBundle(activity, (itemViewHolder?.view as ImageCardView).mainImageView)
                    activity.startActivity(intent, bundle)
                }
                null -> Log.e(TAG, "Item click: Null item")
                else -> Log.e(TAG, "Item click: Invalid item type: ${item::class.java.simpleName}, expected UiVideo")
            }
        }
    }
}