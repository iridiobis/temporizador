package es.iridiobis.temporizador.presentation.ui.runningtask

import android.net.Uri
import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.domain.model.Task


interface RunningTask {
    interface View {
        fun displayName(name : String)
        fun displayBackground(background: Uri)
        fun displayStatus(status : Boolean)
        fun onTaskStopped()
        fun onAlarmGoneOff()
    }

    interface Presenter : Attachable<View> {
        fun pause()
        fun resume()
        fun stop()
    }
}