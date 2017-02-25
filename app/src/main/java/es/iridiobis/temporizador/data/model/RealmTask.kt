package es.iridiobis.temporizador.data.model

import es.iridiobis.temporizador.domain.model.Task
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class RealmTask(
        @PrimaryKey open var id: String? = null,
        open var name: String? = null,
        open var duration: Long? = 0) : RealmObject() {

    constructor(task: Task) : this(task.id, task.name, task.duration)

}