package es.iridiobis.temporizador.presentation.services

import android.app.IntentService
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import android.util.Log
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.domain.services.AlarmService
import es.iridiobis.temporizador.presentation.ui.finishedtask.FinishedTaskActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FireAlarmService : IntentService("FireAlarmService") {
    @Inject lateinit var alarmService: AlarmService

    override fun onCreate() {
        super.onCreate()
        (application as ComponentProvider<ApplicationComponent>).getComponent().inject(this)
    }

    override fun onHandleIntent(intent: Intent?) {

        alarmService.getRunningTask()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    application.startActivity(
                            FinishedTaskActivity.newIntent(it!!.id, this)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    // Release the wake lock provided by the BroadcastReceiver.
                    WakefulBroadcastReceiver.completeWakefulIntent(intent)
                }, { Log.d("AlarmMediaService", "Fired non-existent task") })
    }
}

