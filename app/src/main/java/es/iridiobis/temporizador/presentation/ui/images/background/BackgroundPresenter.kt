package es.iridiobis.temporizador.presentation.ui.images.background

import android.net.Uri
import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.domain.repositories.StringsRepository
import es.iridiobis.temporizador.presentation.ui.model.TaskModel
import javax.inject.Inject

class BackgroundPresenter @Inject constructor(
        val task : TaskModel,
        val navigator: Background.Navigator,
        val stringsRepository: StringsRepository)
    : Presenter<Background.View>(), Background.Presenter {

    var invalid = false

    override fun onViewAttached() {
        if (task.background != null) {
            view?.showBackground(task.background!!, invalid)
            invalid = false
        }
    }

    override fun pickImage() {
        navigator.pickImage()
    }

    override fun cropBackground(origin: Uri) {
        navigator.cropForBackground(origin)
    }

    override fun background(background: Uri) {
        task.background = background
        if (hasView()) {
            view!!.showBackground(background, true)
        } else {
            invalid = true
        }
    }

    override fun next() {
        if (task.background == null) {
            view?.showError(stringsRepository.getString(R.string.background_mandatory))
        } else {
            navigator.backgroundSelected()
        }
    }

}