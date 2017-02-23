package es.iridiobis.temporizador.presentation.ui.addTask

import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.repositories.TasksRepository


class AddTaskPresenter(tasksRepository: TasksRepository) : Presenter<AddTask.View>(), AddTask.Presenter {
    override fun onViewAttached() {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}