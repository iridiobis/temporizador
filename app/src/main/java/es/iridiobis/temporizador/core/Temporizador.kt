package es.iridiobis.temporizador.core

import android.app.Application
import es.iridiobis.temporizador.data.DataModule

class Temporizador : Application() {

    lateinit var component : ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder().dataModule(DataModule()).build()
    }
}
