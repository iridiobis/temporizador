package es.iridiobis.temporizador.domain.services

import es.iridiobis.temporizador.domain.model.Task

/**
 * Handle running and paused notifications and their cancellation
 */
interface TaskNotification {
    fun showRunningNotification(it: Task)
    fun showPausedNotification(it: Task)
    fun cancel()
}