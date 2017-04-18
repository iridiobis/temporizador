package es.iridiobis.temporizador.presentation.ui.finishedtask

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.ActivityScope

@Module
class FinishedTaskModule {
    @Provides
    @ActivityScope
    fun providePresenter(presenter: FinishedTaskPresenter) : FinishedTask.Presenter = presenter
}