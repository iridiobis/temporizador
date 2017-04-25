package es.iridiobis.temporizador.presentation.ui.edittask

import android.net.Uri
import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import es.iridiobis.temporizador.presentation.ui.model.TaskModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class EditTaskPresenter @Inject constructor(
        val task: TaskModel,
        val tasksRepository: TasksRepository,
        val navigator: EditTask.Navigator) : Presenter<EditTask.View>(), EditTask.Presenter {

    override fun name(name: String) {
        task.name = name
        view?.enableSave(task.isValid())
    }

    override fun selectDuration() = view!!.showDurationSelection(task.duration)

    override fun duration(duration: Long) {
        task.duration = duration
        view?.displayTask(task)
        view?.enableSave(task.isValid())
    }

    override fun save() {
        if (task.isValid()) {
            tasksRepository.writeTask(task.id, task.name!!, task.duration, task.background!!, task.smallBackground!!, task.thumbnail!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        navigator.finish()
                    })
        } else {
            view?.showErrorMessage()
        }
    }

    override fun background(background: Uri) {
        task.background = background
    }

    override fun image(image: Uri) {
        task.smallBackground = image
    }

    override fun thumbnail(thumbnail: Uri) {
        task.thumbnail = thumbnail
    }

    override fun onViewAttached() = view!!.displayTask(task)

}