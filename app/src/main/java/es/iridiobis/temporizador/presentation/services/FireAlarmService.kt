package es.iridiobis.temporizador.presentation.services

import android.app.IntentService
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import android.util.Log
import es.iridiobis.temporizador.core.Temporizador
import es.iridiobis.temporizador.domain.services.TaskService
import es.iridiobis.temporizador.presentation.ui.finishedtask.FinishedTaskActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FireAlarmService : IntentService("FireAlarmService") {
    @Inject lateinit var taskService: TaskService

    override fun onCreate() {
        super.onCreate()
        (application as Temporizador).getComponent().inject(this)
    }

    override fun onHandleIntent(intent: Intent) {
        taskService.getRunningTask()
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
                }, { Log.d("FireAlarmService", "Fired non-existent task") })
    }
}

