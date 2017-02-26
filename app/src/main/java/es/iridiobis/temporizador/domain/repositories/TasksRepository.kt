package es.iridiobis.temporizador.domain.repositories

import android.net.Uri
import es.iridiobis.temporizador.domain.model.Task
import io.reactivex.Observable

interface TasksRepository {
    fun retrieveTasks() : Observable<List<Task>>
    //TODO add the image when the images logic is ready
    fun createTask(name: String, duration: Long, background : Uri, smallBackground : Uri, thumbnail : Uri) : Observable<Task>
}
