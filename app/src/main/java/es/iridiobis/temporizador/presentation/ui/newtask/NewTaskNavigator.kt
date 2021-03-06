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

    override fun showImageSelection() {
        if (hasView()) {
            view!!.goToImageSelection()
        } else {
            navigation = Runnable { view!!.goToImageSelection() }
        }
    }

    override fun showThumbnailSelection() {
        if (hasView()) {
            view!!.goToThumbnailSelection()
        } else {
            navigation = Runnable { view!!.goToThumbnailSelection() }
        }
    }

    override fun pickImage() {
        if (hasView()) {
            view!!.goToImagePicker()
        } else {
            navigation = Runnable { view!!.goToImagePicker() }
        }
    }

    override fun cropForBackground(origin: Uri) {
        if (hasView()) {
            view!!.goToCropBackground(origin)
        } else {
            navigation = Runnable { view!!.goToCropBackground(origin) }
        }
    }

    override fun backgroundSelected() {
        if (hasView()) {
            view!!.goToImageSelection()
        } else {
            navigation = Runnable { view!!.goToImageSelection() }
        }
    }

    override fun cropForImage(origin: Uri) {
        if (hasView()) {
            view!!.goToCropForImage(origin)
        } else {
            navigation = Runnable { view!!.goToCropForImage(origin) }
        }
    }

    override fun imageSelected() {
        if (hasView()) {
            view!!.goToThumbnailSelection()
        } else {
            navigation = Runnable { view!!.goToThumbnailSelection() }
        }
    }

    override fun cropForThumbnail(origin: Uri) {
        if (hasView()) {
            view!!.goToCropForThumbnail(origin)
        } else {
            navigation = Runnable { view!!.goToCropForThumbnail(origin) }
        }
    }

    override fun thumbnailSelected() {
        if (hasView()) {
            view!!.goToInformationInput()
        } else {
            navigation = Runnable { view!!.goToInformationInput() }
        }
    }

    override fun finish() {
        if (hasView()) {
            view!!.finish()
        } else {
            navigation = Runnable { view!!.finish() }
        }
    }

}