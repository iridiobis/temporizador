package es.iridiobis.temporizador.presentation.ui.finishedtask

import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import es.iridiobis.temporizador.presentation.ui.runningtask.RunningTask
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class FinishedTaskPresenter(val id: Long, val tasksRepository: TasksRepository) : Presenter<FinishedTask.View>(), FinishedTask.Presenter {
    override fun onViewAttached() {
        //TODO Use AlarmService, no need to pass the id
        tasksRepository.retrieveTask(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.displayBackground(it!!.background)
                })
    }
}