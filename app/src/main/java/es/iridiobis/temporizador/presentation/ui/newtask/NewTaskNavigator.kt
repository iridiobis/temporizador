package es.iridiobis.temporizador.presentation.ui.newtask

import android.net.Uri
import es.iridiobis.presenter.Presenter
import javax.inject.Inject


class NewTaskNavigator @Inject constructor() : Presenter<NewTask.NavigationExecutor>(), NewTask.Navigator {

    var navigation : Runnable? = null

    override fun onViewAttached() {
        navigation?.run()
        navigation = null
    }

    override fun pickImage() {
        if (hasView()) {
            view!!.goToImagePicker()
        } else {
            navigation = Runnable { view!!.goToImagePicker() }
        }
    }

    override fun cropBackground(origin: Uri) {
        if (hasView()) {
            view!!.goToCropBackground(origin)
        } else {
            navigation = Runnable { view!!.goToCropBackground(origin) }
        }
    }

    override fun showImageSelection() {
        if (hasView()) {
            view!!.goToImageSelection()
        } else {
            navigation = Runnable { view!!.goToImageSelection() }
        }
    }

    override fun cropBackgroundForImage(background: Uri) {
        if (hasView()) {
            view!!.goToCropBackgroundForImage(background)
        } else {
            navigation = Runnable { view!!.goToCropBackgroundForImage(background) }
        }
    }

}