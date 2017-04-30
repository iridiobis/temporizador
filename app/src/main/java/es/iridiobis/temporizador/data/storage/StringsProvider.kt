package es.iridiobis.temporizador.data.storage

import android.content.Context
import es.iridiobis.temporizador.domain.repositories.StringsRepository
import javax.inject.Inject


class StringsProvider @Inject constructor(val context : Context) : StringsRepository {
    override fun getString(stringId: Int): String {
        return context.getString(stringId)
    }
}