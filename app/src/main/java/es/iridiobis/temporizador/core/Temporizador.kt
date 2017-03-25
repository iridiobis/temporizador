package es.iridiobis.temporizador.core

import android.app.Application
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.data.DataModule
import io.realm.Realm

class Temporizador : Application(), ComponentProvider<ApplicationComponent> {

    private lateinit var component : ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        component = DaggerApplicationComponent.builder()
                .context(this)
                .dataModule(DataModule())
                .build()
        component.inject(this)
    }

    override fun getComponent(): ApplicationComponent = component

}
