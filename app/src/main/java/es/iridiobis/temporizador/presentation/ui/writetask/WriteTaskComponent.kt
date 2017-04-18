package es.iridiobis.temporizador.presentation.ui.writetask

import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ActivityScope

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(WriteTaskModule::class))
interface WriteTaskComponent : MembersInjector<WriteTaskActivity>