package es.iridiobis.temporizador.core

import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.core.alarm.AlarmHandler
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import es.iridiobis.temporizador.domain.services.AlarmService
import javax.inject.Singleton

@Module class CoreModule {
    @Singleton @Provides
    fun provideAlarmService(handler: AlarmHandler) : AlarmService = handler

    @Singleton @Provides
    fun providesNotificationManager(context : Context) : NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Singleton @Provides
    fun providesSharedPreferences(context : Context) : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
}

