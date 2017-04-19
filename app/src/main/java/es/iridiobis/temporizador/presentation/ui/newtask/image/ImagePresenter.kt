package es.iridiobis.temporizador.presentation.ui.newtask.image

import android.net.Uri
import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.presentation.ui.model.TaskModel
import es.iridiobis.temporizador.presentation.ui.newtask.NewTask
import javax.inject.Inject

class ImagePresenter @Inject constructor(val task : TaskModel, val navigator: NewTask.Navigator)
    : Presenter<Image.View>(), Image.Presenter {

    private var invalid = false

    override fun onViewAttached() {
        view?.showBackground(task.background!!)
        if (task.smallBackground != null) {
            view?.showImage(task.smallBackground!!, invalid)
            invalid = false
        }
    }

    override fun image(image: Uri) {
        task.smallBackground = image
        if (hasView()) {
            view?.showImage(image, true)
        } else {
            invalid = true
        }
    }

    override fun cropBackground() {
        cropBackground(task.background!!)
    }

    override fun cropBackground(origin: Uri) {
        navigator.cropBackgroundForImage(origin)
    }

    override fun next() {
        navigator.showThumbnailSelection()
    }

}