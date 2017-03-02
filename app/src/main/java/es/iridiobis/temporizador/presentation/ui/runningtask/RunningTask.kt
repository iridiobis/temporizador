package es.iridiobis.temporizador.presentation.ui.runningtask

import android.net.Uri
import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.domain.model.Task


interface RunningTask {
    interface View {
        fun displayBackground(background: Uri)
    }

    interface Presenter : Attachable<View>
}