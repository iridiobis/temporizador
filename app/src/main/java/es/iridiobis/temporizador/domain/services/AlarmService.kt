package es.iridiobis.temporizador.domain.services

import es.iridiobis.temporizador.domain.model.Task
import io.reactivex.Observable


interface AlarmService {
    fun getRunningTask() : Observable<Task?>
    fun status() : Observable<Boolean>
    fun setAlarm(task: Task)
    fun pauseAlarm()
    fun resumeAlarm()
}