package es.iridiobis.temporizador.presentation.ui.newtask

import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ActivityScope
import es.iridiobis.temporizador.domain.repositories.TasksRepository

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(NewTaskModule::class))
interface NewTaskComponent : MembersInjector<NewTaskActivity> {
    fun provideTaskModel(): TaskModel
    fun provideNavigator(): NewTask.Navigator
    fun provideTasksRepository() : TasksRepository
}