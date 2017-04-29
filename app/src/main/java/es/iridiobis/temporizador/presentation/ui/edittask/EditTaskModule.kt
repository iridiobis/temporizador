package es.iridiobis.temporizador.presentation.ui.edittask

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.ActivityScope
import es.iridiobis.temporizador.presentation.ui.images.background.Background
import es.iridiobis.temporizador.presentation.ui.images.thumbnail.Thumbnail
import es.iridiobis.temporizador.presentation.ui.model.TaskModel
import es.iridiobis.temporizador.presentation.ui.newtask.NewTask

@Module
class EditTaskModule(val taskModel : TaskModel) {

    @Provides
    @ActivityScope
    fun provideTaskModel() : TaskModel = taskModel

    @Provides
    @ActivityScope
    fun provideNavigator(navigator: EditTaskNavigator) : EditTask.Navigator = navigator

    @Provides
    @ActivityScope
    fun provideBackgroundNavigator(navigator: EditTask.Navigator) : Background.Navigator = navigator

    @Provides
    @ActivityScope
    fun provideThumbnailNavigator(navigator: EditTask.Navigator) : Thumbnail.Navigator = navigator
}