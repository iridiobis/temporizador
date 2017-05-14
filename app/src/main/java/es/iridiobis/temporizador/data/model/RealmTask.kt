package es.iridiobis.temporizador.data.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RealmTask(
        @PrimaryKey open var id: Long = 0,
        open var name: String = "",
        open var duration: Long = 0,
        open var background: String = "",
        open var image: String = "",
        open var thumbnail: String = "") : RealmObject()