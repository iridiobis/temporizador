package es.iridiobis.temporizador.core

import android.app.Application
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.core.realm.Migration
import es.iridiobis.temporizador.data.DataModule
import io.realm.Realm
import io.realm.RealmConfiguration

class Temporizador : Application(), ComponentProvider<ApplicationComponent> {

    private lateinit var component : ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        Realm.setDefaultConfiguration(
                RealmConfiguration.Builder()
                        .schemaVersion(1)
                        .migration(Migration())
                        .build()
        )
        component = DaggerApplicationComponent.builder()
                .context(this)
                .dataModule(DataModule())
                .build()
    }

    override fun getComponent(): ApplicationComponent = component

}
