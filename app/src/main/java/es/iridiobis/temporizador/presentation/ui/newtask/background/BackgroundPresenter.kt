package es.iridiobis.temporizador.presentation.ui.newtask.background

import android.net.Uri
import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.presentation.ui.newtask.TaskModel
import javax.inject.Inject

class BackgroundPresenter @Inject constructor(val task : TaskModel) : Presenter<Background.View>(), Background.Presenter {
    override fun background(background: Uri) {
        task.background = background
    }

    override fun onViewAttached() {
        task.background ?: view?.showBackground(task.background!!)
    }
}