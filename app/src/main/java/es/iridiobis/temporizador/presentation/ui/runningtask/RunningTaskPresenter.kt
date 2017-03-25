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

    override fun onViewAttached() {
        alarmService.getRunningTask()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.displayBackground(it!!.background)
                })
        status = alarmService.status()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.displayStatus(it)
                })
    }

    override fun beforeViewDetached() {
        status.dispose()
    }
}