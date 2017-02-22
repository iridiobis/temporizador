package es.iridiobis.temporizador.presentation.ui.main

import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainPresenter(tasksRepository: TasksRepository) : Presenter<Main.View>(), Main.Presenter {

    var tasks: List<Task>? = null

    init {
        if (tasks == null)
            tasksRepository.retrieveTasks()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        tasks = it
                        view?.displayTasks(it)
                    })
    }

    override fun onViewAttached() {
        if (tasks != null) view!!.displayTasks(tasks!!)
    }
}