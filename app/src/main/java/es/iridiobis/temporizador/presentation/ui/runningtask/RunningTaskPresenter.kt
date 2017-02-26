package es.iridiobis.temporizador.presentation.ui.runningtask

import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class RunningTaskPresenter(val id: Long, val tasksRepository: TasksRepository) : Presenter<RunningTask.View>(), RunningTask.Presenter {
    override fun onViewAttached() {
        tasksRepository.retrieveTask(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.displayBackground(it.background)
                })
    }
}