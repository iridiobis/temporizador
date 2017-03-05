package es.iridiobis.temporizador.data.storage

import android.net.Uri
import es.iridiobis.temporizador.data.model.RealmTask
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import io.reactivex.Observable
import io.realm.Realm

class TasksStorage(val imagesStorage: ImagesStorage) : TasksRepository {
    override fun delete(task: Task): Observable<Unit> {
        return Observable.create {
            subscriber ->
            val realm: Realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                realm.where(RealmTask::class.java).equalTo("id", task.id).findAll().deleteAllFromRealm()
            }
            subscriber.onComplete()
            realm.close()
        }
    }

    override fun retrieveTask(id: Long): Observable<Task> {
        return Observable.create {
            subscriber ->
            val realm: Realm = Realm.getDefaultInstance()
            val task = parseTask(realm.where(RealmTask::class.java).equalTo("id", id).findFirst())
            subscriber.onNext(task)
            subscriber.onComplete()
            realm.close()
        }
    }

    override fun retrieveTasks(): Observable<List<Task>> {
        return Observable.create {
            subscriber ->
            val realm: Realm = Realm.getDefaultInstance()
            val tasks = realm.where(RealmTask::class.java).findAll().map { parseTask(it) }
            subscriber.onNext(tasks)
            subscriber.onComplete()
            realm.close()
        }
    }

    override fun writeTask(id: Long?, name: String, duration: Long, background: Uri, smallBackground: Uri, thumbnail: Uri): Observable<Task> {
        return Observable.create {
            subscriber ->
            val realm: Realm = Realm.getDefaultInstance()
            val safeId = id ?: System.currentTimeMillis()
            val task = RealmTask(safeId, name, duration)
            realm.executeTransaction {
                realm.copyToRealmOrUpdate(task)
            }
            imagesStorage.setFullBackground(safeId, background)
            imagesStorage.setSmallBackground(safeId, smallBackground)
            imagesStorage.setThumbnail(safeId, thumbnail)
            subscriber.onNext(parseTask(task))
            subscriber.onComplete()
            realm.close()
        }

    }

    private fun parseTask(realmTask: RealmTask): Task {
        return Task(realmTask.id,
                realmTask.name,
                realmTask.duration,
                imagesStorage.getFullBackground(realmTask.id),
                imagesStorage.getSmallBackground(realmTask.id),
                imagesStorage.getThumbnail(realmTask.id))
    }

}

