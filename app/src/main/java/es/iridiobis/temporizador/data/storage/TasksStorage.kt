package es.iridiobis.temporizador.data.storage

import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import io.reactivex.Observable

class TasksStorage : TasksRepository {

    override fun retrieveTasks(): Observable<Task> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

