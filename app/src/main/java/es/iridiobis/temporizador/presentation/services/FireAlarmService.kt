package es.iridiobis.temporizador.presentation.services

import android.app.IntentService
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import es.iridiobis.temporizador.domain.services.AlarmService
import es.iridiobis.temporizador.presentation.ui.finishedtask.FinishedTaskActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FireAlarmService @Inject constructor(val alarmService: AlarmService) : IntentService("FireAlarmService") {
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
                })
    }
}

