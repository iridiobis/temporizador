package es.iridiobis.temporizador.presentation.ui.newtask

import android.net.Uri
import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.presentation.ui.images.ImagePicker


interface NewTask {

    interface Navigator : Attachable<NavigationExecutor>, ImagePicker {

        fun showImageSelection()
        fun showThumbnailSelection()
        fun showInformationInput()

        fun cropBackground(origin: Uri)
        fun cropBackgroundForImage(background: Uri)
        fun cropForThumbnail(origin: Uri)

        fun finish()
    }

    interface NavigationExecutor {

        fun goToImageSelection()
        fun goToThumbnailSelection()
        fun goToInformationInput()

        fun goToImagePicker()
        fun goToCropBackground(origin: Uri)
        fun goToCropBackgroundForImage(background: Uri)
        fun goToCropForThumbnail(origin: Uri)

        fun finish()
    }

}