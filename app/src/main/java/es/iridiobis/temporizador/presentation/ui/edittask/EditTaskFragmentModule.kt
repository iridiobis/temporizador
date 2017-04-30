package es.iridiobis.temporizador.presentation.ui.edittask

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.FragmentScope

@Module
class EditTaskFragmentModule {
    @Provides
    @FragmentScope
    fun providePresenter(presenter: EditTaskPresenter) : EditTask.Presenter = presenter
}