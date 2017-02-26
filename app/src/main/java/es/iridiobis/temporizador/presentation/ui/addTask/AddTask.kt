package es.iridiobis.temporizador.presentation.ui.addTask

import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.domain.model.Task


interface AddTask {
    interface View {
        fun onTaskAdded(task: Task)
    }

    interface Presenter : Attachable<View> {
        fun name(name: String)
        fun duration(duration: Long)
        fun save()
    }
}