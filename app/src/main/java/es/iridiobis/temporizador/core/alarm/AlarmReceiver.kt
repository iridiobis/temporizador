package es.iridiobis.temporizador.core.alarm

import android.content.Context
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import es.iridiobis.temporizador.presentation.services.FireAlarmService
import es.iridiobis.temporizador.presentation.services.ACTION_PLAY
import es.iridiobis.temporizador.presentation.services.AlarmMediaService


class AlarmReceiver : WakefulBroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val mpIntent = Intent(context, AlarmMediaService::class.java)
        mpIntent.action = ACTION_PLAY
        context?.startService(mpIntent)

        val service = Intent(context, FireAlarmService::class.java)
        WakefulBroadcastReceiver.startWakefulService(context, service)
    }
}