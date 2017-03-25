package es.iridiobis.temporizador.core.alarm

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.preference.PreferenceManager
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.BehaviorRelay.create
import es.iridiobis.kotlinexample.getLong
import es.iridiobis.kotlinexample.getPreferencesEditor
import es.iridiobis.temporizador.core.notification.NotificationProvider
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.services.AlarmService
import io.reactivex.Observable
import javax.inject.Inject

class AlarmHandler @Inject constructor(val tasksStorage: TasksStorage, val notificationProvider: NotificationProvider, val context: Context) : AlarmService {
    var task: Task? = null
    val statusRelay : BehaviorRelay<Boolean> = create<Boolean>()

    override fun hasRunningTask(): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context).contains("TASK")
    }

    override fun getRunningTask(): Observable<Task> {
        if (task != null) {
            return Observable.just(task)
        } else if (hasRunningTask()) {
            return tasksStorage.retrieveTask(context.getLong("TASK"))
                    .map { it -> saveTask(it) }
        } else {
            return Observable.error<Task> { IllegalStateException("No running task") }
        }
    }

    override fun status(): Observable<Boolean> {
        return statusRelay
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
        statusRelay.accept(true)
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
        statusRelay.accept(false)
    }

    override fun resumeAlarm() {
        context.getPreferencesEditor()
                .putLong("START_TIME", System.currentTimeMillis())
                .apply()
        setAlarm(task!!.duration - context.getLong("ELAPSED_TIME"))
        statusRelay.accept(true)
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