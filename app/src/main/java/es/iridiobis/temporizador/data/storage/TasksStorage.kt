package es.iridiobis.temporizador.data.storage

import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import io.reactivex.Observable
import java.util.*

class TasksStorage : TasksRepository {

    override fun retrieveTasks(): Observable<List<Task>> {
        return Observable.just(Collections.singletonList(Task("dummy", "Dummy", 10000)))
    }

}

