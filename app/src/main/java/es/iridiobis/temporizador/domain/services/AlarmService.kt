package es.iridiobis.temporizador.domain.services

import es.iridiobis.temporizador.domain.model.Task


interface AlarmService {
    fun setAlarm(task: Task)
}