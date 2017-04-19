package es.iridiobis.temporizador.presentation.ui.newtask

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.ActivityScope
import es.iridiobis.temporizador.presentation.ui.model.TaskModel

@Module
class NewTaskModule {

    @Provides
    @ActivityScope
    fun provideTask() : TaskModel = TaskModel()

    @Provides
    @ActivityScope
    fun provideNavigator(navigator: NewTaskNavigator) : NewTask.Navigator = navigator
}