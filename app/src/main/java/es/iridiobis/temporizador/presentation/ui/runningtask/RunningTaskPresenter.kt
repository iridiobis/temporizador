package es.iridiobis.temporizador.presentation.ui.runningtask

import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.services.TaskService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class RunningTaskPresenter @Inject constructor(val taskService: TaskService)
    : Presenter<RunningTask.View>(), RunningTask.Presenter {

    lateinit var disposables : CompositeDisposable

    override fun onViewAttached() {
        taskService.getRunningTask()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ it : Task ->
                    view?.displayBackground(it.background)
                    view?.displayName(it.name)
                })
        disposables = CompositeDisposable()
        disposables.add(
                taskService.status()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe { view?.displayStatus(it) }
        )
        disposables.add(
                taskService.remaining()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe { view?.displayRemainingTime(it) }
        )
        disposables.add(
                taskService.next()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({
                            if (it) view?.onAlarmGoneOff() else view?.onTaskStopped()
                        })
        )


    }

    override fun beforeViewDetached() {
        disposables.dispose()
    }


    override fun changeStatus() {
        taskService.changeStatus()
    }

    override fun stop() {
        taskService.stopTask()
        view?.onTaskStopped()
    }

}