package es.iridiobis.temporizador.presentation.ui.newtask.thumbnail

import android.net.Uri
import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.presentation.ui.newtask.ImagePicker


interface Thumbnail {

    interface View {
        fun showBackground(background: Uri)
        fun showThumbnail(thumbnail: Uri, invalid: Boolean)
    }

    interface Presenter : Attachable<View>, ImagePicker {
        fun thumbnail(thumbnail: Uri)
        fun cropBackground()
        fun crop(origin : Uri)
    }

}