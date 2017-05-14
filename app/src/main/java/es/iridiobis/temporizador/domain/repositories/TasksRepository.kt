package es.iridiobis.temporizador.domain.repositories

import android.net.Uri
import es.iridiobis.temporizador.domain.model.Task
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface TasksRepository {
    fun retrieveTasks() : Observable<List<Task>>
    fun writeTask(id: Long?, name: String, duration: Long, background : Uri, smallBackground : Uri, thumbnail : Uri) : Observable<Task>
    fun retrieveTask(id : Long) : Single<Task>
    fun delete(task: Task) : Completable
}
