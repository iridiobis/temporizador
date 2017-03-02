package es.iridiobis.temporizador.presentation.services

import android.app.IntentService
import android.content.Intent
import android.preference.PreferenceManager
import android.support.v4.content.WakefulBroadcastReceiver
import es.iridiobis.temporizador.data.storage.ImagesStorage
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.presentation.ui.finishedtask.FinishedTaskActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FireAlarmService : IntentService("FireAlarmService") {
    override fun onHandleIntent(intent: Intent?) {
        TasksStorage(ImagesStorage(applicationContext)).retrieveTask(PreferenceManager.getDefaultSharedPreferences(this).getLong("TASK", 0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    application.startActivity(
                            FinishedTaskActivity.newIntent(it.id, this)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                    // Release the wake lock provided by the BroadcastReceiver.
                    WakefulBroadcastReceiver.completeWakefulIntent(intent)
                })
    }
}

