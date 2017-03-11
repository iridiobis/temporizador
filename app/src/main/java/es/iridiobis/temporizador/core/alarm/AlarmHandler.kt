package es.iridiobis.temporizador.core.alarm

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.preference.PreferenceManager
import es.iridiobis.temporizador.core.notification.NotificationProvider
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.services.AlarmService
import es.iridiobis.temporizador.presentation.services.AlarmMediaService
import io.reactivex.Observable
import javax.inject.Inject

class AlarmHandler @Inject constructor(val tasksStorage: TasksStorage, val notificationProvider: NotificationProvider, val context: Context) : AlarmService {
    var task: Task? = null
    override fun getRunningTask(): Observable<Task?> {
        if (task != null) {
            return Observable.just(task)
        } else {
            return tasksStorage.retrieveTask(PreferenceManager.getDefaultSharedPreferences(context).getLong("TASK", 0))
        }
    }

    override fun setAlarm(task: Task) {
        this.task = task
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putLong("TASK", task.id)
                .putLong("START_TIME", System.currentTimeMillis())
                .apply()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(context, 0, AlarmReceiver.playIntent(context), 0)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + task.duration, alarmIntent)
        else
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + task.duration, alarmIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationProvider.notificationId, notificationProvider.showRunningNotification(task))
    }
    
}