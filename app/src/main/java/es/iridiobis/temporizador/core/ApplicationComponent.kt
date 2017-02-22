package es.iridiobis.temporizador.core

import dagger.Component
import es.iridiobis.temporizador.data.DataModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DataModule::class))
interface ApplicationComponent