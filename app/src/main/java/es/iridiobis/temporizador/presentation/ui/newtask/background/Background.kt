package es.iridiobis.temporizador.presentation.ui.newtask.background

import android.net.Uri
import es.iridiobis.presenter.Attachable

interface Background {

    interface View {
        fun showBackground(background: Uri)
    }

    interface Presenter : Attachable<View> {
        fun background(background: Uri)
    }

}