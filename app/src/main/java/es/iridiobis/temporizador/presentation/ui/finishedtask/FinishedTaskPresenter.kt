package es.iridiobis.temporizador.presentation.ui.finishedtask

import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.services.AlarmService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class FinishedTaskPresenter @Inject constructor(val alarmService: AlarmService) : Presenter<FinishedTask.View>(), FinishedTask.Presenter {

    lateinit var nextDisposable : Disposable

    override fun onViewAttached() {
        view?.soundAlarm()

        alarmService.getRunningTask()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { view?.displayBackground(it!!.background) }

        nextDisposable = alarmService.next()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { view?.onAlarmFinished() }

    }

    override fun beforeViewDetached() {
        nextDisposable.dispose()
    }

    override fun finishAlarm() {
        alarmService.stopAlarm()
    }
}