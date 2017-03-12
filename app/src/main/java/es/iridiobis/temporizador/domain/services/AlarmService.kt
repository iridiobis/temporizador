package es.iridiobis.temporizador.domain.services

import es.iridiobis.temporizador.domain.model.Task
import io.reactivex.Observable


interface AlarmService {
    fun getRunningTask() : Observable<Task?>
    fun setAlarm(task: Task)
    fun pauseAlarm()
}