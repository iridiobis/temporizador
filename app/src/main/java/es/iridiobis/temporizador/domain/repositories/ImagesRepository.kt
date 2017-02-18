package es.iridiobis.temporizador.domain.repositories

import android.net.Uri


interface ImagesRepository {
    fun getFullBackground(id : String) : Uri
    fun getSmallBackground(id : String) : Uri
}