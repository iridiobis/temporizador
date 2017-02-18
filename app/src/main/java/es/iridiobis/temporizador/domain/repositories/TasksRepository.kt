package es.iridiobis.temporizador.domain.repositories

import es.iridiobis.temporizador.domain.model.Task
import io.reactivex.Observable

interface TasksRepository {
    fun retrieveTasks() : Observable<Task>
}
