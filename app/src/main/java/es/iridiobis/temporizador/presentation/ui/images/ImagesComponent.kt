package es.iridiobis.temporizador.presentation.ui.images

import es.iridiobis.temporizador.domain.repositories.StringsRepository
import es.iridiobis.temporizador.presentation.ui.images.background.Background
import es.iridiobis.temporizador.presentation.ui.images.thumbnail.Thumbnail
import es.iridiobis.temporizador.presentation.ui.model.TaskModel

interface ImagesComponent {
    fun provideTaskModel(): TaskModel
    fun provideThumbnailNavigator() : Thumbnail.Navigator
    fun provideBackgroundNavigator() : Background.Navigator
    fun provideStringsRepository() : StringsRepository
}