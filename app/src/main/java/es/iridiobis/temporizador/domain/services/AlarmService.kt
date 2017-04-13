package es.iridiobis.temporizador.domain.services

import es.iridiobis.temporizador.domain.model.Task
import io.reactivex.Observable

interface AlarmService {
    fun hasRunningTask() : Observable<Boolean>
    fun hasGoneOff() : Boolean
    fun getRunningTask() : Observable<Task>
    fun status() : Observable<Boolean>
    fun next() : Observable<Boolean>
    fun startTask(task: Task)
    fun pauseTask()
    fun resumeTask()
    fun stopTask()
    fun playAlarm()
    fun stopAlarm()
}