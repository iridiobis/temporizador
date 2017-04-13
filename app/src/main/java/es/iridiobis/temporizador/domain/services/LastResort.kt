package es.iridiobis.temporizador.domain.services

/**
 * Handles actions that require a context when no activity is there to handle them
 */
interface LastResort {
    /**
     * Shows the finished task screen.
     */
    fun goToFinishedScreen()

    /**
     * Stops the alarm
     */
    fun stopAlarm()
}