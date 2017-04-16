package es.iridiobis.temporizador.presentation.ui.newtask.thumbnail

import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.core.di.FragmentScope
import es.iridiobis.temporizador.presentation.ui.newtask.NewTaskComponent

@FragmentScope
@Component(dependencies = arrayOf(NewTaskComponent::class), modules = arrayOf(ThumbnailModule::class))
interface ThumbnailComponent : MembersInjector<ThumbnailFragment>
