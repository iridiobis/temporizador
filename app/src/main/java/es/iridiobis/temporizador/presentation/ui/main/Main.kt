package es.iridiobis.temporizador.presentation.ui.main

import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.domain.model.Task


interface Main {
    interface View {
        fun displayTasks(tasks: List<Task>)
    }
    interface Presenter : Attachable<View> {
        fun runTask(task: Task)
        fun delete(task: Task)
    }
}