package es.iridiobis.temporizador.presentation.ui.edittask

import android.net.Uri
import es.iridiobis.presenter.Attachable
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.presentation.ui.model.TaskModel


interface EditTask {
    interface View {
        fun displayTask(task: TaskModel)
        fun showErrorMessage()
        fun onTaskAdded(task: Task)
    }

    interface Presenter : Attachable<View> {
        fun name(name: String)
        fun duration(duration: Long)
        fun background(background: Uri)
        fun processCrop(cropImage: Uri) : Boolean
        fun save()
    }
}