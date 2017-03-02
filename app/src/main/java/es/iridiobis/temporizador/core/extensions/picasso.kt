package es.iridiobis.temporizador.core.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import com.squareup.picasso.Target
import es.iridiobis.temporizador.BuildConfig.DEBUG

fun ImageView.load(path: Uri, request: (RequestCreator) -> RequestCreator) {
    val picasso = Picasso.with(context)
    if(DEBUG) {
        picasso.setIndicatorsEnabled(true)
        picasso.isLoggingEnabled = true
    }
    request(picasso.load(path)).into(this)
}

fun View.setBackground(path: Uri, request: (RequestCreator) -> RequestCreator) {
    val picasso = Picasso.with(context)
    if(DEBUG) {
        picasso.setIndicatorsEnabled(true)
        picasso.isLoggingEnabled = true
    }
    val target = object : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) = Unit

        override fun onBitmapFailed(errorDrawable: Drawable?) = Unit

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            this@setBackground.background = BitmapDrawable(context.resources, bitmap)
        }
    }
    tag = target
    request(picasso.load(path)).into(target)
}