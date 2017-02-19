package es.iridiobis.temporizador.presentation.services

import android.app.IntentService
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import es.iridiobis.temporizador.presentation.ui.finishedtask.FinishedTaskActivity

class FireAlarmService : IntentService("FireAlarmService") {
    override fun onHandleIntent(intent: Intent?) {
        application.startActivity(
                Intent(applicationContext, FinishedTaskActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        // Release the wake lock provided by the BroadcastReceiver.
        WakefulBroadcastReceiver.completeWakefulIntent(intent)
    }
}

