package es.iridiobis.temporizador.presentation.ui.newtask.background

import android.net.Uri
import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.presentation.ui.model.TaskModel
import es.iridiobis.temporizador.presentation.ui.newtask.NewTask
import javax.inject.Inject

class BackgroundPresenter @Inject constructor(val task : TaskModel, val navigator: NewTask.Navigator)
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
        navigator.cropBackground(origin)
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
        navigator.showImageSelection()
    }

}