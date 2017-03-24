package es.iridiobis.temporizador.core.alarm

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.preference.PreferenceManager
import es.iridiobis.kotlinexample.getLong
import es.iridiobis.kotlinexample.getPreferencesEditor
import es.iridiobis.temporizador.core.notification.NotificationProvider
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.services.AlarmService
import io.reactivex.Observable
import javax.inject.Inject
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus

class AlarmHandler @Inject constructor(val tasksStorage: TasksStorage, val notificationProvider: NotificationProvider, val context: Context) : AlarmService {
    var task: Task? = null
    override fun getRunningTask(): Observable<Task?> {
        if (task != null) {
            return Observable.just(task)
        } else {
            return tasksStorage.retrieveTask(PreferenceManager.getDefaultSharedPreferences(context).getLong("TASK", 0))
                    .map { it -> saveTask(it) }

        }
    }

    private fun saveTask(task : Task?) : Task? {
        this.task = task
        return task
    }

    override fun setAlarm(task: Task) {
        this.task = task
        context.getPreferencesEditor()
                .putLong("TASK", task.id)
                .putLong("START_TIME", System.currentTimeMillis())
                .apply()
        setAlarm(task.duration)
    }

    override fun pauseAlarm() {
        val elapsetTime = context.getLong("ELAPSED_TIME") + System.currentTimeMillis() - context.getLong("START_TIME")
        context.getPreferencesEditor()
                .putLong("START_TIME", 0)
                .putLong("ELAPSED_TIME", elapsetTime)
                .apply()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(context, 0, AlarmReceiver.playIntent(context), 0)
        alarmManager.cancel(alarmIntent)
        notify(notificationProvider.showPausedNotification(task!!))
    }

    override fun resumeAlarm() {
        context.getPreferencesEditor()
                .putLong("START_TIME", System.currentTimeMillis())
                .apply()
        setAlarm(task!!.duration - context.getLong("ELAPSED_TIME"))
    }

    private fun setAlarm(remaining : Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(context, 0, AlarmReceiver.playIntent(context), 0)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + remaining, alarmIntent)
        else
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + remaining, alarmIntent)

        notify(notificationProvider.showRunningNotification(task!!))
    }

    private fun notify(notification : Notification) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationProvider.notificationId, notification)
    }

}