package es.iridiobis.temporizador.presentation.ui.newtask

import android.net.Uri
import es.iridiobis.presenter.Attachable


interface NewTask {

    interface Navigator : Attachable<NavigationExecutor>, ImagePicker {
        fun cropBackground(origin: Uri)
        fun showImageSelection()
        fun showThumbnailSelection()
        fun cropBackgroundForImage(background: Uri)
        fun cropForThumbnail(origin: Uri)
    }

    interface NavigationExecutor {
        fun goToImagePicker()
        fun goToCropBackground(origin: Uri)
        fun goToImageSelection()
        fun goToThumbnailSelection()
        fun goToCropBackgroundForImage(background: Uri)
        fun goToCropForThumbnail(origin: Uri)
    }

}