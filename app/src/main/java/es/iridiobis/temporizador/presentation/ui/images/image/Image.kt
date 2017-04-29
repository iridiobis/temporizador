package es.iridiobis.temporizador.presentation.ui.images.image

import android.net.Uri
import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.presentation.ui.images.ImagePicker

interface Image {

    interface View {
        fun showBackground(background: Uri)
        fun showImage(image: Uri, invalid : Boolean)
        fun showError(message : String)
    }

    interface Presenter : Attachable<View>, ImagePicker {
        fun image(image: Uri)
        fun cropBackground()
        fun cropBackground(origin : Uri)
        fun next()
    }

    interface Navigator : ImagePicker {
        fun cropForImage(origin: Uri)
        fun imageSelected()
    }

}