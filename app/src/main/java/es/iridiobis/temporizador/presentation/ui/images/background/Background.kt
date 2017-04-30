package es.iridiobis.temporizador.presentation.ui.images.background

import android.net.Uri
import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.presentation.ui.images.ImagePicker

interface Background {

    interface View {
        fun showBackground(background: Uri, invalid : Boolean)
        fun showError(message : String)
    }

    interface Presenter : Attachable<View>, ImagePicker {
        fun background(background: Uri)
        fun next()
        fun cropBackground(origin: Uri)
    }

    interface Navigator : ImagePicker {
        fun cropForBackground(origin: Uri)
        fun backgroundSelected()
    }

}