package es.iridiobis.temporizador.presentation.ui.edittask

import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.core.di.FragmentScope

@FragmentScope
@Component(dependencies = arrayOf(EditTaskComponent::class), modules = arrayOf(EditTaskFragmentModule::class))
interface EditTaskFragmentComponent : MembersInjector<EditTaskFragment>
