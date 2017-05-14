package es.iridiobis.temporizador.presentation.ui.newtask.information

import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import es.iridiobis.temporizador.presentation.ui.model.TaskModel
import es.iridiobis.temporizador.presentation.ui.newtask.NewTask
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class InformationPresenter @Inject constructor(
        val task: TaskModel,
        val tasksRepository: TasksRepository,
        val navigator: NewTask.Navigator)
    : Presenter<Information.View>(), Information.Presenter {

    override fun onViewAttached() {
        view?.displayTask(task)
    }

    override fun name(name: String) {
        task.name = name
    }

    override fun selectDuration() {
        view?.showDurationSelection(task.duration)
    }

    override fun duration(duration: Long) {
        task.duration = duration
    }

    override fun save() {
        if (task.isValid()) {
            tasksRepository.createTask(task.name!!, task.duration, task.background!!, task.smallBackground!!, task.thumbnail!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe { navigator.finish() }
        } else {
            view?.showErrorMessage()
        }
    }

}