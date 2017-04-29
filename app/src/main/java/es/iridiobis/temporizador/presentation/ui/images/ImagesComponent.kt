package es.iridiobis.temporizador.presentation.ui.images

import es.iridiobis.temporizador.domain.repositories.StringsRepository
import es.iridiobis.temporizador.presentation.ui.images.background.Background
import es.iridiobis.temporizador.presentation.ui.images.image.Image
import es.iridiobis.temporizador.presentation.ui.images.thumbnail.Thumbnail
import es.iridiobis.temporizador.presentation.ui.model.TaskModel

interface ImagesComponent {
    fun provideTaskModel(): TaskModel
    fun provideBackgroundNavigator() : Background.Navigator
    fun provideImageNavigator() : Image.Navigator
    fun provideThumbnailNavigator() : Thumbnail.Navigator
    fun provideStringsRepository() : StringsRepository
}