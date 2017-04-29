package es.iridiobis.temporizador.presentation.ui.images.thumbnail

import android.net.Uri
import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.domain.repositories.StringsRepository
import es.iridiobis.temporizador.presentation.ui.model.TaskModel
import javax.inject.Inject

class ThumbnailPresenter @Inject constructor(
        val task : TaskModel,
        val navigator: Thumbnail.Navigator,
        val stringsRepository: StringsRepository)
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
        navigator.pickImage()
    }

    override fun cropBackground() {
        crop(task.background!!)
    }

    override fun crop(origin: Uri) {
        navigator.cropForThumbnail(origin)
    }

    override fun next() {
        if (task.thumbnail == null) {
            view?.showError(stringsRepository.getString(R.string.image_mandatory))
        }
        navigator.thumbnailSelected()
    }

}