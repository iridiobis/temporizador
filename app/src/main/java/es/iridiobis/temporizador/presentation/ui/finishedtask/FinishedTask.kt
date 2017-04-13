package es.iridiobis.temporizador.presentation.ui.finishedtask

import android.net.Uri
import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.domain.model.Task


interface FinishedTask {
    interface View {
        fun displayBackground(background: Uri)
        fun onAlarmFinished()
    }

    interface Presenter : Attachable<View> {
        fun finishAlarm()
    }

}