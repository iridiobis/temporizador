package es.iridiobis.temporizador.presentation.ui.newtask.image

import android.net.Uri
import es.iridiobis.presenter.Attachable


interface Image {

    interface View {
        fun showBackground(background: Uri)
        fun showImage(image: Uri, invalid : Boolean)
    }

    interface Presenter : Attachable<View> {
        fun image(image: Uri)
        fun cropBackground()
        fun cropBackground(origin : Uri)
    }

}