package es.iridiobis.temporizador.presentation.ui.newtask

import android.net.Uri
import es.iridiobis.presenter.Attachable


interface NewTask {

    interface Navigator : Attachable<NavigationExecutor> {
        fun showImageSelection()
    }

    interface NavigationExecutor {
        fun goToImageSelection()
    }

}