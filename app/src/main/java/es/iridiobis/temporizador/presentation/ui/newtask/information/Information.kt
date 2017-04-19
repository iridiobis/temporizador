package es.iridiobis.temporizador.presentation.ui.newtask.information

import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.presentation.ui.model.TaskModel

interface Information {

    interface View {
        fun displayTask(task: TaskModel)
        fun enableSave(enabled : Boolean)
        fun showDurationSelection(duration: Long)
    }

    interface Presenter : Attachable<View> {
        fun name(name: String)
        fun duration(duration: Long)
        fun save()
        fun selectDuration()
    }
}