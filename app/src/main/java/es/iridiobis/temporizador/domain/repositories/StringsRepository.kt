package es.iridiobis.temporizador.domain.repositories


interface StringsRepository {
    fun getString(stringId : Int) : String
}