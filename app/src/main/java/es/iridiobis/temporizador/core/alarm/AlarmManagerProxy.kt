package es.iridiobis.temporizador.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.SystemClock
import es.iridiobis.temporizador.domain.services.AlarmService
import javax.inject.Inject


class AlarmManagerProxy @Inject constructor(val context: Context) : AlarmService {
    override fun setAlarm(remaining: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(context, 0, AlarmReceiver.playAlarmIntent(context), 0)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + remaining, alarmIntent)
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + remaining, alarmIntent)
        else
            alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + remaining, alarmIntent)
    }

    override fun cancelAlarm() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(context, 0, AlarmReceiver.playAlarmIntent(context), 0)
        alarmManager.cancel(alarmIntent)
    }
}