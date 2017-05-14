package es.iridiobis.temporizador.presentation.ui.edittask

import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ActivityScope
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import es.iridiobis.temporizador.presentation.ui.images.ImagesComponent
import es.iridiobis.temporizador.presentation.ui.model.TaskModel

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(EditTaskModule::class))
interface EditTaskComponent : MembersInjector<EditTaskActivity>, ImagesComponent {
    fun provideNavigator(): EditTask.Navigator
    fun provideTasksRepository() : TasksRepository
}