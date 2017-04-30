package es.iridiobis.temporizador.presentation.ui.edittask

import android.net.Uri
import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.core.di.ActivityScope
import javax.inject.Inject

@ActivityScope
class EditTaskNavigator @Inject constructor() : Presenter<EditTask.NavigationExecutor>(), EditTask.Navigator {

    var navigation : Runnable? = null

    override fun onViewAttached() {
        navigation?.run()
        navigation = null
    }

    override fun showBackgroundSelection() {
        if (hasView()) {
            view!!.goToBackgroundSelection()
        } else {
            navigation = Runnable { view!!.goToBackgroundSelection() }
        }
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
            view!!.goToCropForBackground(origin)
        } else {
            navigation = Runnable { view!!.goToCropForBackground(origin) }
        }
    }

    override fun backgroundSelected() {
        if (hasView()) {
            view!!.goBack()
        } else {
            navigation = Runnable { view!!.goBack() }
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
            view!!.goBack()
        } else {
            navigation = Runnable { view!!.goBack() }
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
            view!!.goBack()
        } else {
            navigation = Runnable { view!!.goBack() }
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