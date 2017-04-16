package es.iridiobis.temporizador.presentation.ui.newtask.background

import android.net.Uri
import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.presentation.ui.newtask.NewTask
import es.iridiobis.temporizador.presentation.ui.newtask.TaskModel
import javax.inject.Inject

class BackgroundPresenter @Inject constructor(val task : TaskModel, val navigator: NewTask.Navigator)
    : Presenter<Background.View>(), Background.Presenter {

    override fun onViewAttached() {
        if (task.background != null) view?.showBackground(task.background!!)
    }

    override fun pickImage() {
        navigator.pickImage()
    }

    override fun cropBackground(origin: Uri) {
        navigator.cropBackground(origin)
    }

    override fun background(background: Uri) {
        task.background = background
        view?.showBackground(background)
    }

    override fun next() {
        navigator.showImageSelection()
    }

}