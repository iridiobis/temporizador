package es.iridiobis.temporizador.presentation.ui.edittask

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.ActivityScope

@Module
class WriteTaskModule(val taskId : Long?) {

    @Provides
    @ActivityScope
    fun provideTaskId() : Long? = taskId

    @Provides
    @ActivityScope
    fun providePresenter(presenter: WriteTaskPresenter) : WriteTask.Presenter = presenter
}