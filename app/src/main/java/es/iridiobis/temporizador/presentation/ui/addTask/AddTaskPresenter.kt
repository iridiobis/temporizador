package es.iridiobis.temporizador.presentation.ui.addTask

import android.net.Uri
import es.iridiobis.presenter.Presenter
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class AddTaskPresenter(val tasksRepository: TasksRepository) : Presenter<AddTask.View>(), AddTask.Presenter {

    var name : String = ""
    var duration : Long = 0
    var background : Uri? = null
    var smallBackground: Uri? = null
    var thumbnail: Uri? = null

    override fun name(name: String) {
        this.name = name
    }

    override fun duration(duration: Long) {
        this.duration = duration
    }

    override fun save() {
        //TODO validation
        tasksRepository.createTask(name, duration, background!!, smallBackground!!, thumbnail!!)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    view?.onTaskAdded(it)
                })
    }

    override fun background(background: Uri) {
        this.background = background
        view?.displayBackground(background)
    }

    override fun processCrop(cropImage: Uri) : Boolean {
        smallBackground?.let {
            thumbnail = cropImage
            return false
        } ?: let {
            smallBackground = cropImage
            return true
        }
    }

    override fun onViewAttached() {
        background?.let { view?.displayBackground(background!!) }
        smallBackground?.let { view?.displaySmallBackground(smallBackground!!) }
        thumbnail?.let { view?.displayThumbnail(thumbnail!!) }
    }
}