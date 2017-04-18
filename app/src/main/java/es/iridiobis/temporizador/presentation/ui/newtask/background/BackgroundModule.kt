package es.iridiobis.temporizador.presentation.ui.newtask.background

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.FragmentScope

@Module
class BackgroundModule {
    @Provides
    @FragmentScope
    fun providePresenter(presenter: BackgroundPresenter) : Background.Presenter = presenter
}