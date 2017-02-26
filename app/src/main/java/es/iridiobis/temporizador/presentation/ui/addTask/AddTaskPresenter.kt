package es.iridiobis.temporizador.presentation.ui.addTask

import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AddTaskPresenter(val tasksRepository: TasksRepository) : Presenter<AddTask.View>(), AddTask.Presenter {
    var name : String = ""
    var duration : Long = 0

    override fun name(name: String) {
        this.name = name
    }

    override fun duration(duration: Long) {
        this.duration = duration
    }

    override fun save() {
        tasksRepository.addTask(name, duration)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.onTaskAdded(it)
                })
    }

    override fun onViewAttached() {
        //nothing to do
    }
}