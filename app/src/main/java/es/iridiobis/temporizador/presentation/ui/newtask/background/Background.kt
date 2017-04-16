package es.iridiobis.temporizador.presentation.ui.newtask.background

import android.net.Uri
import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.presentation.ui.newtask.ImagePicker

interface Background {

    interface View {
        fun showBackground(background: Uri)
    }

    interface Presenter : Attachable<View>, ImagePicker {
        fun background(background: Uri)
        fun next()
        fun cropBackground(origin: Uri)
    }

}