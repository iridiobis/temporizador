package es.iridiobis.temporizador.presentation.ui.newtask

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

}