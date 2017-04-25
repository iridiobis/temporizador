package es.iridiobis.temporizador.presentation.ui.images.thumbnail

import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.core.di.FragmentScope
import es.iridiobis.temporizador.presentation.ui.images.ImagesComponent
import es.iridiobis.temporizador.presentation.ui.newtask.NewTaskComponent

@FragmentScope
@Component(dependencies = arrayOf(ImagesComponent::class), modules = arrayOf(ThumbnailModule::class))
interface ThumbnailComponent : MembersInjector<ThumbnailFragment>
