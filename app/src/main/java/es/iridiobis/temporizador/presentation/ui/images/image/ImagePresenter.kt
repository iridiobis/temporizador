package es.iridiobis.temporizador.presentation.ui.images.image

import android.net.Uri
import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.domain.repositories.StringsRepository
import es.iridiobis.temporizador.presentation.ui.model.TaskModel
import javax.inject.Inject

class ImagePresenter @Inject constructor(
        val task : TaskModel,
        val navigator: Image.Navigator,
        val stringsRepository: StringsRepository)
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

    override fun pickImage() {
        navigator.pickImage()
    }

    override fun cropBackground() {
        cropBackground(task.background!!)
    }

    override fun cropBackground(origin: Uri) {
        navigator.cropForImage(origin)
    }

    override fun next() {
        if (task.smallBackground == null) {
            view?.showError(stringsRepository.getString(R.string.image_mandatory))
        } else {
            navigator.imageSelected()
        }
    }

}