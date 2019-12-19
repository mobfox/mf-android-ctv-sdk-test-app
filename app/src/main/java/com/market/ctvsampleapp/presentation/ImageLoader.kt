package com.market.ctvsampleapp.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes

interface ImageLoader {

    fun loadImage(
        context: Context,
        url: String,
        width: Int,
        height: Int,
        errorDrawable: Drawable?,
        setFunction: (resource: Drawable) -> Unit
    )

    fun loadImage(
        context: Context,
        url: String,
        errorDrawable: Drawable?,
        destination: ImageView
    )

    fun loadBitmapImage(
        context: Context,
        url: String,
        @DrawableRes errorDrawable: Int,
        setFunction: (bitmap: Bitmap) -> Unit
    )

    fun loadImage(
        context: Context,
        url: String,
        width: Int,
        height: Int,
        @DrawableRes errorDrawable: Int,
        setFunction: (drawable: Drawable) -> Unit
    )

}