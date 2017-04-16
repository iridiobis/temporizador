package es.iridiobis.temporizador.presentation.ui.newtask

import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ActivityScope

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(NewTaskModule::class))
interface NewTaskComponent : MembersInjector<NewTaskActivity> {
    fun provideTaskModel(): TaskModel
    fun provideNavigator(): NewTask.Navigator
}