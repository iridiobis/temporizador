package es.iridiobis.temporizador.data.storage

import android.net.Uri
import es.iridiobis.temporizador.domain.repositories.ImagesRepository


class ImagesStorage : ImagesRepository {
    override fun getFullBackground(id: String): Uri {
        return Uri.parse("android.resource://es.iridiobis.temporizador/mipmap/ic_launcher")
    }

    override fun getSmallBackground(id: String): Uri {
        return Uri.parse("android.resource://es.iridiobis.temporizador/mipmap/ic_launcher")
    }
}
