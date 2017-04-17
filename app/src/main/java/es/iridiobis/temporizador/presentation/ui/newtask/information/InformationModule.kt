package es.iridiobis.temporizador.presentation.ui.newtask.information

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.FragmentScope

@Module
class InformationModule {
    @Provides
    @FragmentScope
    fun providePresenter(presenter: InformationPresenter) : Information.Presenter = presenter
}