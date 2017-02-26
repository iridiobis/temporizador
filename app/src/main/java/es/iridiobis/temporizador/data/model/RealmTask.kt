package es.iridiobis.temporizador.data.model

import es.iridiobis.temporizador.domain.model.Task
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class RealmTask(
        @PrimaryKey open var id: Long = 0,
        open var name: String = "",
        open var duration: Long = 0) : RealmObject() {

    constructor(task: Task) : this(task.id, task.name, task.duration)

    fun parse() = Task(id, name, duration)
}