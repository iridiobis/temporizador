package es.iridiobis.temporizador.presentation.ui.newtask

import dagger.Component
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ActivityScope

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(NewTaskModule::class))
interface NewTaskComponent {
    fun provideTaskModel() : TaskModel
}