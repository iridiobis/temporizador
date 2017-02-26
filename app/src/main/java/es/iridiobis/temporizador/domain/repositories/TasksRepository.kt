package es.iridiobis.temporizador.domain.repositories

import es.iridiobis.temporizador.domain.model.Task
import io.reactivex.Observable

interface TasksRepository {
    fun retrieveTasks() : Observable<List<Task>>
    //TODO add the image when the images logic is ready
    fun addTask(name: String, duration: Long) : Observable<Task>
}
