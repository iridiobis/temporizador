package es.iridiobis.temporizador.presentation.ui.runningtask

import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.services.AlarmService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class RunningTaskPresenter @Inject constructor(val alarmService: AlarmService)
    : Presenter<RunningTask.View>(), RunningTask.Presenter {

    lateinit var status : Disposable
    lateinit var continueDisposable : Disposable

    override fun onViewAttached() {
        alarmService.getRunningTask()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.displayBackground(it!!.background)
                    view?.displayName(it!!.name)
                })
        status = alarmService.status()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.displayStatus(it)
                })
        continueDisposable = alarmService.next()
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


    override fun pause() {
        alarmService.pauseTask()
    }

    override fun resume() {
        alarmService.resumeTask()
    }

    override fun stop() {
        alarmService.stopTask()
        view?.onTaskStopped()
    }

}