package es.iridiobis.temporizador.presentation.ui.runningtask

import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.services.TaskService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class RunningTaskPresenter @Inject constructor(val taskService: TaskService)
    : Presenter<RunningTask.View>(), RunningTask.Presenter {

    lateinit var status : Disposable
    lateinit var continueDisposable : Disposable

    override fun onViewAttached() {
        taskService.getRunningTask()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.displayBackground(it!!.background)
                    view?.displayName(it!!.name)
                })
        status = taskService.status()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.displayStatus(it)
                })
        continueDisposable = taskService.next()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it) view?.onAlarmGoneOff() else view?.onTaskStopped()
                })

    }

    override fun beforeViewDetached() {
        status.dispose()
        continueDisposable.dispose()
    }


    override fun changeStatus() {
        taskService.changeStatus()
    }

    override fun stop() {
        taskService.stopTask()
        view?.onTaskStopped()
    }

}