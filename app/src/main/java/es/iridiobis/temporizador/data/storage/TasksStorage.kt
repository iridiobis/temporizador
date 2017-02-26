package es.iridiobis.temporizador.data.storage

import es.iridiobis.temporizador.data.model.RealmTask
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import io.reactivex.Observable
import io.realm.Realm

class TasksStorage : TasksRepository {

    override fun retrieveTasks(): Observable<List<Task>> {
        return Observable.create {
            subscriber ->
            val realm : Realm = Realm.getDefaultInstance()
            val tasks = realm.where(RealmTask::class.java).findAll().map(RealmTask::parse)
            subscriber.onNext(tasks)
            subscriber.onComplete()
            realm.close()
        }
    }

    override fun addTask(name: String, duration: Long): Observable<Task> {
        return Observable.create {
            subscriber ->
            val realm : Realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                val id = System.currentTimeMillis()
                val task = RealmTask(id, name, duration)
                realm.copyToRealm(task)
                subscriber.onNext(task.parse())
                subscriber.onComplete()
            }
            realm.close()
        }

    }

}

