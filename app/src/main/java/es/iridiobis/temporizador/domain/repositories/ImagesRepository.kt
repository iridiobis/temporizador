package es.iridiobis.temporizador.domain.repositories

import android.net.Uri


interface ImagesRepository {
    fun getFullBackground(id : Long) : Uri
    fun getSmallBackground(id : Long) : Uri
}