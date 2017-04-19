package es.iridiobis.temporizador.presentation.ui.edittask

import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ActivityScope

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(EditTaskModule::class))
interface EditTaskComponent : MembersInjector<EditTaskActivity>