package es.iridiobis.temporizador.core.alarm

import android.content.Context
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import es.iridiobis.temporizador.domain.services.LastResort
import es.iridiobis.temporizador.presentation.services.AlarmMediaService
import es.iridiobis.temporizador.presentation.services.FireAlarmService
import javax.inject.Inject


class LastResortManager @Inject constructor(val context: Context) : LastResort {

    override fun goToFinishedScreen() {
        WakefulBroadcastReceiver.startWakefulService(context, Intent(context, FireAlarmService::class.java))
    }

    override fun stopAlarm() {
        context.stopService(Intent(context, AlarmMediaService::class.java))
    }

}