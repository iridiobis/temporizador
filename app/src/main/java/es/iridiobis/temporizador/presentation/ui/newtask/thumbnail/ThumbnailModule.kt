package es.iridiobis.temporizador.presentation.ui.newtask.thumbnail

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.FragmentScope

@Module
class ThumbnailModule {
    @Provides
    @FragmentScope
    fun providePresenter(presenter: ThumbnailPresenter) : Thumbnail.Presenter = presenter
}