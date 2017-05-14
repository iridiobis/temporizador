package es.iridiobis.temporizador.domain.repositories

import android.net.Uri
import es.iridiobis.temporizador.domain.model.Task
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface TasksRepository {
    fun retrieveTasks() : Observable<List<Task>>
    fun createTask(name: String, duration: Long, background : Uri, smallBackground : Uri, thumbnail : Uri) : Completable
    fun editTask(task: Task) : Completable
    fun retrieveTask(id : Long) : Single<Task>
    fun delete(task: Task) : Completable
}
