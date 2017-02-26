package es.iridiobis.temporizador.core

import android.app.Application
import es.iridiobis.temporizador.data.DataModule
import io.realm.Realm

class Temporizador : Application() {

    lateinit var component : ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        component = DaggerApplicationComponent.builder().dataModule(DataModule()).build()
    }
}
