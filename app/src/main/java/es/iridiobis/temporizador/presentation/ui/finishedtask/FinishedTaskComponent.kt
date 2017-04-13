package es.iridiobis.temporizador.presentation.ui.finishedtask

import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ActivityScope


@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(FinishedTaskModule::class))
interface FinishedTaskComponent : MembersInjector<FinishedTaskActivity>