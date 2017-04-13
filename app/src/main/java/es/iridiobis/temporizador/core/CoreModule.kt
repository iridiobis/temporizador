package es.iridiobis.temporizador.core

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.alarm.LastResortManager
import es.iridiobis.temporizador.domain.services.AlarmService
import es.iridiobis.temporizador.domain.services.LastResort
import javax.inject.Singleton

@Module class CoreModule {
    @Singleton @Provides

    @Singleton @Provides
    fun provideNotificationManager(context : Context) : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Singleton @Provides
    fun provideSharedPreferences(context : Context) : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Singleton @Provides
    fun provideLastResort(lastResort: LastResortManager) : LastResort = lastResort
}

