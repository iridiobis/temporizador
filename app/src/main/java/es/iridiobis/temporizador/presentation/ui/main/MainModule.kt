package es.iridiobis.temporizador.presentation.ui.main

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.ActivityScope

@Module
class MainModule {
    @Provides
    @ActivityScope
    fun providePresenter(presenter: MainPresenter) : Main.Presenter = presenter
}