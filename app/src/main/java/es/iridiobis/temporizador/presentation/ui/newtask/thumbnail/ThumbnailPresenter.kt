package es.iridiobis.temporizador.presentation.ui.newtask.thumbnail

import android.net.Uri
import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.presentation.ui.newtask.NewTask
import es.iridiobis.temporizador.presentation.ui.newtask.TaskModel
import javax.inject.Inject

class ThumbnailPresenter @Inject constructor(val task : TaskModel, val navigator: NewTask.Navigator)
    : Presenter<Thumbnail.View>(), Thumbnail.Presenter {

    private var invalid = false

    override fun onViewAttached() {
        view?.showBackground(task.background!!)
        if (task.thumbnail != null) {
            view?.showThumbnail(task.thumbnail!!, invalid)
            invalid = false
        }
    }

    override fun thumbnail(thumbnail: Uri) {
        task.thumbnail = thumbnail
        if (hasView()) {
            view?.showThumbnail(thumbnail, true)
        } else {
            invalid = true
        }
    }

    override fun pickImage() {
        navigator.showImageSelection()
    }

    override fun cropBackground() {
        crop(task.background!!)
    }

    override fun crop(origin: Uri) {
        navigator.cropForThumbnail(origin)
    }

}