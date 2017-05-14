package es.iridiobis.temporizador.presentation.ui.main

import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import es.iridiobis.temporizador.domain.services.TaskService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MainPresenter @Inject constructor(val tasksRepository: TasksRepository, val taskService: TaskService) : Presenter<Main.View>(), Main.Presenter {

    override fun runTask(task: Task) {
        taskService.startTask(task)
    }

    override fun delete(task: Task) {
        tasksRepository.delete(task)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({onViewAttached()})
    }

    override fun onViewAttached() {
        tasksRepository.retrieveTasks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.displayTasks(it)
                })
    }
}