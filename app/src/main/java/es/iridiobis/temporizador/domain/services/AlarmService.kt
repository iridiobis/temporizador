package es.iridiobis.temporizador.domain.services

interface AlarmService {
    fun setAlarm(remaining: Long)
    fun cancelAlarm()
}