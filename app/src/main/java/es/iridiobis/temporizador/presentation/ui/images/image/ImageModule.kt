package es.iridiobis.temporizador.presentation.ui.images.image

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.FragmentScope

@Module
class ImageModule {
    @Provides
    @FragmentScope
    fun providePresenter(presenter: ImagePresenter) : Image.Presenter = presenter
}