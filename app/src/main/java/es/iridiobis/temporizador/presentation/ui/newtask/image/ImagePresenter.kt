package es.iridiobis.temporizador.presentation.ui.newtask.image

import android.net.Uri
import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.presentation.ui.newtask.TaskModel
import es.iridiobis.temporizador.presentation.ui.newtask.background.Background
import javax.inject.Inject

class ImagePresenter @Inject constructor(val task : TaskModel) : Presenter<Image.View>(), Image.Presenter {

    override fun onViewAttached() {
        view?.showBackground(task.background!!)
        if (task.smallBackground != null) view?.showImage(task.smallBackground!!)
    }

    override fun image(image: Uri) {
        task.smallBackground = image
        view?.showImage(image)
    }

}