package com.market.ctvsampleapp.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget

class GlideImageLoader : ImageLoader {

    override fun loadImage(
        context: Context,
        url: String,
        width: Int,
        height: Int,
        errorDrawable: Drawable?,
        setFunction: (resource: Drawable) -> Unit
    ) {
        Glide.with(context)
            .load(url)
            .centerCrop()
            .error(errorDrawable)
            .into<SimpleTarget<GlideDrawable>>(
                object : SimpleTarget<GlideDrawable>(width, height) {
                    override fun onResourceReady(
                        resource: GlideDrawable,
                        glideAnimation: GlideAnimation<in GlideDrawable>
                    ) {
                        setFunction.invoke(resource)
                    }
                })
    }

    override fun loadImage(context: Context, url: String, errorDrawable: Drawable?, destination: ImageView) {
        Glide.with(context)
            .load(url)
            .centerCrop()
            .error(errorDrawable)
            .into(destination)
    }

    override fun loadImage(context: Context, url: String, width: Int, height: Int, errorDrawable: Int, setFunction: (drawable: Drawable) -> Unit) {
        Glide.with(context)
            .load(url)
            .centerCrop()
            .error(errorDrawable)
            .into<SimpleTarget<GlideDrawable>>(object : SimpleTarget<GlideDrawable>(width, height) {
                override fun onResourceReady(resource: GlideDrawable, glideAnimation: GlideAnimation<in GlideDrawable>) {
                    setFunction.invoke(resource)
                }
            })
    }

    override fun loadBitmapImage(context: Context, url: String, errorDrawable: Int, setFunction: (bitmap: Bitmap) -> Unit) {
        Glide.with(context)
            .load(url)
            .asBitmap()
            .centerCrop()
            .error(errorDrawable)
            .into<SimpleTarget<Bitmap>>(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                   setFunction.invoke(bitmap)
                }
            })
    }
}