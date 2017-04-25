package es.iridiobis.temporizador.presentation.ui.edittask

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.di.ActivityScope
import es.iridiobis.temporizador.presentation.ui.model.TaskModel

@Module
class EditTaskModule(val taskModel : TaskModel) {

    @Provides
    @ActivityScope
    fun provideTaskModel() : TaskModel = taskModel

    @Provides
    @ActivityScope
    fun provideNavigator(navigator: EditTaskNavigator) : EditTask.Navigator = navigator

}