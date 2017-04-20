package es.iridiobis.temporizador.presentation.ui.images.background

import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.core.di.FragmentScope
import es.iridiobis.temporizador.presentation.ui.newtask.NewTaskComponent


@FragmentScope
@Component(dependencies = arrayOf(NewTaskComponent::class), modules = arrayOf(BackgroundModule::class))
interface BackgroundComponent : MembersInjector<BackgroundFragment>
