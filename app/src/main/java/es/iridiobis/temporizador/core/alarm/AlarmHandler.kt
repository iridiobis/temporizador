package es.iridiobis.temporizador.core.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.services.AlarmService


class AlarmHandler(val context : Context) : AlarmService {
    override fun setAlarm(task: Task) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(context, 0, Intent(context, AlarmReceiver::class.java), 0)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + task.duration, alarmIntent)
        else
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + task.duration, alarmIntent)
    }
}