package es.iridiobis.temporizador.domain.services

import android.content.SharedPreferences
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
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
    var endMillis = 0L
    val status = AtomicBoolean()
    val statusRelay: BehaviorRelay<Boolean> = BehaviorRelay.create()
    val continueRelay: PublishRelay<Boolean> = PublishRelay.create()

    fun hasRunningTask(): Single<Boolean> {
        if (task == null && !preferences.contains(TASK_PREFERENCE)) {
            return Single.just(false)
        } else if (preferences.contains(TASK_PREFERENCE)) {
            return tasksRepository.retrieveTask(preferences.getLong(TASK_PREFERENCE, 0))
                    .map {
                        initializeTask(it)
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
                        initializeTask(it)
                        it
                    }
        } else {
            return Single.error<Task> { IllegalStateException("No running task") }
        }
    }

    private fun initializeTask(task: Task) {
        this.task = task
        endMillis = task.duration - preferences.getLong(ELAPSED_TIME_PREFERENCE, 0) + preferences.getLong(START_TIME_PREFERENCE, 0)
        status.set(preferences.getBoolean(RUNNING_PREFERENCE, false))
        statusRelay.accept(status.get())
    }

    fun status(): Observable<Boolean> {
        return statusRelay
    }

    fun remaining(): Observable<Long> {
        return Observable.concat(
                Observable.just(
                        if (status.get())
                            endMillis - System.currentTimeMillis()
                        else
                            task!!.duration - preferences.getLong(ELAPSED_TIME_PREFERENCE, 0)
                ),
                Observable.interval(1, 1, TimeUnit.SECONDS, Schedulers.io())
                        .filter { status.get() }
                        .map { endMillis - System.currentTimeMillis() }
        )
    }

    fun next(): Observable<Boolean> {
        return continueRelay
    }

    fun startTask(task: Task) {
        this.task = task
        val remaining = task.duration
        val currentTime = System.currentTimeMillis()
        endMillis = remaining + currentTime
        preferences.edit()
                .putLong(TASK_PREFERENCE, task.id)
                .putLong(START_TIME_PREFERENCE, currentTime)
                .putBoolean(RUNNING_PREFERENCE, true)
                .apply()
        setAlarm(task.duration)
        status.set(true)
        statusRelay.accept(status.get())
    }

    fun changeStatus() {
        if (status.get()) pauseTask() else resumeTask()
        status.set(!status.get())
        statusRelay.accept(status.get())
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
        val remaining = task!!.duration - preferences.getLong(ELAPSED_TIME_PREFERENCE, 0)
        val currentTime = System.currentTimeMillis()
        endMillis = remaining + currentTime
        setAlarm(remaining)
        preferences.edit()
                .putLong(START_TIME_PREFERENCE, currentTime)
                .putBoolean(RUNNING_PREFERENCE, true)
                .apply()
    }

    private fun clearTask() {
        task = null
        status.set(false)
        preferences.edit()
                .clear()
                .apply()
    }

}