package es.iridiobis.temporizador.presentation.ui.newtask

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.ActivityScope

@Module
class NewTaskModule {

    @Provides
    @ActivityScope
    fun provideTask() : TaskModel = TaskModel()
}