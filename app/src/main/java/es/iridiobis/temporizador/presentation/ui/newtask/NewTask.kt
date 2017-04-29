package es.iridiobis.temporizador.presentation.ui.newtask

import android.net.Uri
import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.presentation.ui.images.ImagePicker
import es.iridiobis.temporizador.presentation.ui.images.background.Background
import es.iridiobis.temporizador.presentation.ui.images.image.Image
import es.iridiobis.temporizador.presentation.ui.images.thumbnail.Thumbnail


interface NewTask {

    interface Navigator : Attachable<NavigationExecutor>, Background.Navigator, Image.Navigator, Thumbnail.Navigator {

        fun showImageSelection()
        fun showThumbnailSelection()
        fun finish()
    }

    interface NavigationExecutor {

        fun goToImageSelection()
        fun goToThumbnailSelection()
        fun goToInformationInput()

        fun goToImagePicker()
        fun goToCropBackground(origin: Uri)
        fun goToCropForImage(origin: Uri)
        fun goToCropForThumbnail(origin: Uri)

        fun finish()
    }

}