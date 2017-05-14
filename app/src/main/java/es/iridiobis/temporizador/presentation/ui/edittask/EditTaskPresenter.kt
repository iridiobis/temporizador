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
    }

    override fun selectDuration() = view!!.showDurationSelection(task.duration)

    override fun duration(duration: Long) {
        task.duration = duration
        view?.displayTask(task)
    }

    override fun save() {
        if (task.isComplete()) {
            tasksRepository.editTask(task.parse())
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

    override fun selectBackground() {
        navigator.showBackgroundSelection()
    }

    override fun image(image: Uri) {
        task.smallBackground = image
    }

    override fun selectImage() {
        navigator.showImageSelection()
    }

    override fun thumbnail(thumbnail: Uri) {
        task.thumbnail = thumbnail
    }

    override fun selectThumbnail() {
        navigator.showThumbnailSelection()
    }

    override fun onViewAttached() = view!!.displayTask(task)

}