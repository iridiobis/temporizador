package es.iridiobis.temporizador.presentation.ui.main

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.ActivityScope
import es.iridiobis.temporizador.presentation.ui.runningtask.RunningTask
import es.iridiobis.temporizador.presentation.ui.runningtask.RunningTaskPresenter

@Module
class RunningTaskModule {
    @Provides
    @ActivityScope
    fun providePresenter(presenter: RunningTaskPresenter) : RunningTask.Presenter = presenter
}