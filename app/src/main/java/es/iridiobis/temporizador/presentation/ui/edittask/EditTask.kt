package es.iridiobis.temporizador.presentation.ui.edittask

import android.net.Uri
import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.presentation.ui.images.ImagePicker
import es.iridiobis.temporizador.presentation.ui.images.background.Background
import es.iridiobis.temporizador.presentation.ui.images.thumbnail.Thumbnail
import es.iridiobis.temporizador.presentation.ui.model.TaskModel


interface EditTask {
    interface View {
        fun displayTask(task: TaskModel)
        fun showErrorMessage()
        fun showDurationSelection(duration: Long)
    }

    interface Presenter : Attachable<View> {
        fun name(name: String)
        fun duration(duration: Long)
        fun background(background: Uri)
        fun image(image: Uri)
        fun thumbnail(thumbnail: Uri)
        fun save()
        fun selectBackground()
        fun selectThumbnail()
        fun selectDuration()
    }

    interface Navigator : Attachable<NavigationExecutor>, Background.Navigator, Thumbnail.Navigator {
        fun showBackgroundSelection()
        fun showImageSelection()
        fun showThumbnailSelection()

        fun cropBackgroundForImage(background: Uri)

        fun finish()
    }

    interface NavigationExecutor {
        fun goToBackgroundSelection()
        fun goToImageSelection()
        fun goToThumbnailSelection()

        fun goToImagePicker()
        fun goToCropBackground(origin: Uri)
        fun goToCropBackgroundForImage(background: Uri)
        fun goToCropForThumbnail(origin: Uri)

        fun goBack()
        fun finish()
    }
}