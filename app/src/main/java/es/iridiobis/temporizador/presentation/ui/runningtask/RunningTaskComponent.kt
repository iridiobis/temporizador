package es.iridiobis.temporizador.presentation.ui.main

import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ActivityScope
import es.iridiobis.temporizador.presentation.ui.runningtask.RunningTaskActivity

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(RunningTaskModule::class))
interface RunningTaskComponent : MembersInjector<RunningTaskActivity>