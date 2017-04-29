package es.iridiobis.temporizador.presentation.ui.newtask

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.ActivityScope
import es.iridiobis.temporizador.presentation.ui.images.background.Background
import es.iridiobis.temporizador.presentation.ui.images.thumbnail.Thumbnail
import es.iridiobis.temporizador.presentation.ui.model.TaskModel

@Module
class NewTaskModule {

    @Provides
    @ActivityScope
    fun provideTask() : TaskModel = TaskModel()

    @Provides
    @ActivityScope
    fun provideNavigator(navigator: NewTaskNavigator) : NewTask.Navigator = navigator

    @Provides
    @ActivityScope
    fun provideBackgroundNavigator(navigator: NewTask.Navigator) : Background.Navigator = navigator

    @Provides
    @ActivityScope
    fun provideThumbnailNavigator(navigator: NewTask.Navigator) : Thumbnail.Navigator = navigator
}