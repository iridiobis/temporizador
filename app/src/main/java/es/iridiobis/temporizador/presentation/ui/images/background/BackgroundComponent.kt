package es.iridiobis.temporizador.presentation.ui.images.background

import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.core.di.FragmentScope
import es.iridiobis.temporizador.presentation.ui.images.ImagesComponent


@FragmentScope
@Component(dependencies = arrayOf(ImagesComponent::class), modules = arrayOf(BackgroundModule::class))
interface BackgroundComponent : MembersInjector<BackgroundFragment>
