package es.iridiobis.temporizador.domain.services

/**
 * Allows to set and cancel the alarm.
 */
interface AlarmService {
    fun setAlarm(remaining: Long)
    fun cancelAlarm()
}