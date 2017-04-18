package es.iridiobis.temporizador.presentation.ui.main

import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ActivityScope

@ActivityScope
@Component(dependencies = arrayOf(ApplicationComponent::class), modules = arrayOf(MainModule::class))
interface MainComponent : MembersInjector<MainActivity>