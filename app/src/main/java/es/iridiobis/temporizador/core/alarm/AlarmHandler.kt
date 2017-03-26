package es.iridiobis.temporizador.core.alarm

import android.app.NotificationManager
import android.content.SharedPreferences
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.BehaviorRelay.create
import es.iridiobis.temporizador.core.notification.NotificationProvider
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.services.AlarmService
import io.reactivex.Observable
import javax.inject.Inject

class AlarmHandler @Inject constructor(
        val tasksStorage: TasksStorage,
        val notificationProvider: NotificationProvider,
        val preferences: SharedPreferences,
        val alarmManagerProxy: AlarmManagerProxy,
        val notificationManager: NotificationManager) : AlarmService {

    companion object {
        private val TASK_PREFERENCE : String = "TASK"
        private val GONE_OFF_PREFERENCE : String = "GONE_OFF"
        private val START_TIME_PREFERENCE : String = "START_TIME"
        private val ELLAPSED_TIME_PREFERENCE : String = "ELLAPSED_TIME"
    }
    var task: Task? = null
    val statusRelay: BehaviorRelay<Boolean> = create<Boolean>()

    override fun hasRunningTask(): Observable<Boolean> {
        if (task == null && !preferences.contains(TASK_PREFERENCE)) {
            return Observable.just(false)
        } else if (preferences.contains(TASK_PREFERENCE)) {
            return tasksStorage.retrieveTask(preferences.getLong(TASK_PREFERENCE, 0))
                    .map {
                        this.task = it
                        true
                    }
                    .doOnError {
                        clearAlarm()
                        Observable.just(false)
                    }
        } else {
            return Observable.just(true)
        }
    }

    override fun hasGoneOff(): Boolean {
        return task != null && preferences.contains(GONE_OFF_PREFERENCE)
    }

    override fun getRunningTask(): Observable<Task> {
        if (task != null) {
            return Observable.just(task)
        } else if (preferences.contains(TASK_PREFERENCE)) {
            return tasksStorage.retrieveTask(preferences.getLong(TASK_PREFERENCE, 0))
                    .map {
                        this.task = it
                        it
                    }
        } else {
            return Observable.error<Task> { IllegalStateException("No running task") }
        }
    }

    override fun status(): Observable<Boolean> {
        return statusRelay
    }

    override fun setAlarm(task: Task) {
        this.task = task
        preferences.edit()
                .putLong(TASK_PREFERENCE, task.id)
                .putLong(START_TIME_PREFERENCE, System.currentTimeMillis())
                .apply()
        setAlarm(task.duration)
        statusRelay.accept(true)
    }

    override fun pauseAlarm() {
        val elapsetTime = preferences.getLong(ELLAPSED_TIME_PREFERENCE, 0) + System.currentTimeMillis() - preferences.getLong(START_TIME_PREFERENCE, 0)
        preferences.edit()
                .putLong(START_TIME_PREFERENCE, 0)
                .putLong(ELLAPSED_TIME_PREFERENCE, elapsetTime)
                .apply()
        alarmManagerProxy.cancelAlarm()
        notificationManager.notify(
                notificationProvider.notificationId,
                notificationProvider.showPausedNotification(task!!)
        )
        statusRelay.accept(false)
    }

    override fun resumeAlarm() {
        preferences.edit()
                .putLong(START_TIME_PREFERENCE, System.currentTimeMillis())
                .apply()
        setAlarm(task!!.duration - preferences.getLong(ELLAPSED_TIME_PREFERENCE, 0))
        statusRelay.accept(true)
    }

    override fun playAlarm() {
        preferences.edit()
                .putBoolean(GONE_OFF_PREFERENCE, true)
                .apply()
    }

    override fun clearAlarm() {
        preferences.edit()
                .clear()
                .apply()
    }

    private fun setAlarm(remaining: Long) {
        alarmManagerProxy.setAlarm(remaining)
        notificationManager.notify(
                notificationProvider.notificationId,
                notificationProvider.showRunningNotification(task!!)
        )
    }

}