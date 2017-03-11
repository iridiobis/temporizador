package es.iridiobis.temporizador.presentation.ui.writetask

import android.net.Uri
import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class WriteTaskPresenter(val id: Long?, val tasksRepository: TasksRepository) : Presenter<WriteTask.View>(), WriteTask.Presenter {

    val task = TaskModel()

    init {
        id?.let {
            tasksRepository.retrieveTask(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        task.id = it.id
                        task.name = it.name
                        task.duration = it.duration
                        task.background = it.background
                        task.smallBackground = it.smallBackground
                        task.thumbnail = it.thumbnail
                        view?.displayTask(task)
                    })
        }
    }

    override fun name(name: String) {
        task.name = name
    }

    override fun duration(duration: Long) {
        task.duration = duration
    }

    override fun save() {
        if (task.isValid()) {
            tasksRepository.writeTask(task.id, task.name!!, task.duration, task.background!!, task.smallBackground!!, task.thumbnail!!)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        view?.onTaskAdded(it)
                    })
        } else {
            view?.showErrorMessage()
        }
    }

    override fun background(background: Uri) {
        task.background = background
        task.smallBackground = null
        task.thumbnail = null
    }

    override fun processCrop(cropImage: Uri) : Boolean {
        task.smallBackground?.let {
            task.thumbnail = cropImage
            return false
        } ?: let {
            task.smallBackground = cropImage
            return true
        }
    }

    override fun onViewAttached() {
        view?.displayTask(task)
    }
}