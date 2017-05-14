package es.iridiobis.temporizador.data.storage

import android.net.Uri
import es.iridiobis.temporizador.data.model.RealmTask
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import javax.inject.Inject

class TasksStorage @Inject constructor(val imagesStorage: ImagesStorage) : TasksRepository {
    override fun delete(task: Task): Completable {
        return Completable.create {
            subscriber ->
            val realm: Realm = Realm.getDefaultInstance()
            realm.executeTransaction {
                realm.where(RealmTask::class.java).equalTo("id", task.id).findAll().deleteAllFromRealm()
            }
            imagesStorage.deleteImage(task.background)
            imagesStorage.deleteImage(task.smallBackground)
            imagesStorage.deleteImage(task.thumbnail)
            realm.close()
            subscriber.onComplete()
        }
    }

    override fun retrieveTask(id: Long): Single<Task> {
        return Single.create {
            subscriber ->
            val realm: Realm = Realm.getDefaultInstance()
            val realmTask: RealmTask = realm.where(RealmTask::class.java).equalTo("id", id).findFirst()
            val task = parseTask(realmTask)
            realm.close()
            subscriber.onSuccess(task)
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

    override fun createTask(name: String, duration: Long, background: Uri, smallBackground: Uri, thumbnail: Uri): Completable {
        return Completable.create {
            subscriber ->
            val realm: Realm = Realm.getDefaultInstance()
            val safeId = System.currentTimeMillis()
            val task = RealmTask(
                    safeId,
                    name,
                    duration,
                    imagesStorage.setBackground(safeId, background),
                    imagesStorage.setImage(safeId, smallBackground),
                    imagesStorage.setThumbnail(safeId, thumbnail)
            )
            realm.executeTransaction {
                realm.copyToRealmOrUpdate(task)
            }
            subscriber.onComplete()
            realm.close()
        }

    }

    override fun editTask(task: Task): Completable {
        return Completable.create {
            subscriber ->
            val realm: Realm = Realm.getDefaultInstance()
            val oldRealmTask: RealmTask = realm.where(RealmTask::class.java).equalTo("id", task.id).findFirst()
            val realmTask = RealmTask(
                    task.id,
                    task.name,
                    task.duration,
                    imagesStorage.setBackground(task.id, task.background, oldRealmTask.background),
                    imagesStorage.setImage(task.id, task.smallBackground, oldRealmTask.image),
                    imagesStorage.setThumbnail(task.id, task.thumbnail, oldRealmTask.thumbnail)
            )
            realm.executeTransaction {
                realm.copyToRealmOrUpdate(realmTask)
            }
            realm.close()
            subscriber.onComplete()
        }

    }

    private fun parseTask(realmTask: RealmTask): Task {
        return Task(realmTask.id,
                realmTask.name,
                realmTask.duration,
                imagesStorage.getUri(realmTask.background),
                imagesStorage.getUri(realmTask.image),
                imagesStorage.getUri(realmTask.thumbnail))
    }

}

