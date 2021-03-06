package es.iridiobis.temporizador.domain.services

import android.content.SharedPreferences
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskService @Inject constructor(
        val tasksRepository: TasksRepository,
        val taskNotification: TaskNotification,
        val preferences: SharedPreferences,
        val alarmService: AlarmService,
        val lastResort: LastResort) {

    companion object {
        private val TASK_PREFERENCE: String = "TASK"
        private val GONE_OFF_PREFERENCE: String = "GONE_OFF"
        private val START_TIME_PREFERENCE: String = "START_TIME"
        private val ELAPSED_TIME_PREFERENCE: String = "ELAPSED_TIME"
        private val RUNNING_PREFERENCE: String = "RUNNING"
    }

    var task: Task? = null
    var status: Boolean = false
    val statusRelay: BehaviorRelay<Boolean> = BehaviorRelay.create()
    val continueRelay: PublishRelay<Boolean> = PublishRelay.create()

    fun hasRunningTask(): Single<Boolean> {
        if (task == null && !preferences.contains(TASK_PREFERENCE)) {
            return Single.just(false)
        } else if (preferences.contains(TASK_PREFERENCE)) {
            return tasksRepository.retrieveTask(preferences.getLong(TASK_PREFERENCE, 0))
                    .map {
                        this.task = it
                        status = preferences.getBoolean(RUNNING_PREFERENCE, false)
                        statusRelay.accept(status)
                        true
                    }
                    .doOnError {
                        clearTask()
                        Observable.just(false)
                    }
        } else {
            return Single.just(true)
        }
    }

    fun hasGoneOff(): Boolean {
        return task != null && preferences.contains(GONE_OFF_PREFERENCE)
    }

    fun getRunningTask(): Single<Task> {
        if (task != null) {
            return Single.just(task)
        } else if (preferences.contains(TASK_PREFERENCE)) {
            return tasksRepository.retrieveTask(preferences.getLong(TASK_PREFERENCE, 0))
                    .map {
                        this.task = it
                        statusRelay.accept(preferences.getBoolean(RUNNING_PREFERENCE, false))
                        it
                    }
        } else {
            return Single.error<Task> { IllegalStateException("No running task") }
        }
    }

    fun status(): Observable<Boolean> {
        return statusRelay
    }

    fun next(): Observable<Boolean> {
        return continueRelay
    }

    fun startTask(task: Task) {
        this.task = task
        preferences.edit()
                .putLong(TASK_PREFERENCE, task.id)
                .putLong(START_TIME_PREFERENCE, System.currentTimeMillis())
                .putBoolean(RUNNING_PREFERENCE, true)
                .apply()
        setAlarm(task.duration)
        status = true
        statusRelay.accept(status)
    }

    fun changeStatus() {
        if (status) pauseTask() else resumeTask()
        status = !status
        statusRelay.accept(status)
    }

    fun stopTask() {
        clearTask()
        alarmService.cancelAlarm()
        taskNotification.cancel()
        continueRelay.accept(false)
    }

    fun playAlarm() {
        preferences.edit()
                .putBoolean(GONE_OFF_PREFERENCE, true)
                .apply()
        if (continueRelay.hasObservers()) {
            continueRelay.accept(true)
        } else {
            lastResort.goToFinishedScreen()
        }
    }

    fun stopAlarm() {
        clearTask()
        if (continueRelay.hasObservers()) {
            continueRelay.accept(true)
        } else {
            lastResort.stopAlarm()
        }
    }

    private fun setAlarm(remaining: Long) {
        alarmService.setAlarm(remaining)
        taskNotification.showRunningNotification(task!!)
    }

    private fun pauseTask() {
        val elapsedTime = preferences.getLong(ELAPSED_TIME_PREFERENCE, 0) + System.currentTimeMillis() - preferences.getLong(START_TIME_PREFERENCE, 0)
        preferences.edit()
                .putLong(START_TIME_PREFERENCE, 0)
                .putLong(ELAPSED_TIME_PREFERENCE, elapsedTime)
                .putBoolean(RUNNING_PREFERENCE, false)
                .apply()
        alarmService.cancelAlarm()
        taskNotification.showPausedNotification(task!!)
    }

    private fun resumeTask() {
        preferences.edit()
                .putLong(START_TIME_PREFERENCE, System.currentTimeMillis())
                .putBoolean(RUNNING_PREFERENCE, true)
                .apply()
        setAlarm(task!!.duration - preferences.getLong(ELAPSED_TIME_PREFERENCE, 0))
    }

    private fun clearTask() {
        task = null
        status = false
        preferences.edit()
                .clear()
                .apply()
    }

}