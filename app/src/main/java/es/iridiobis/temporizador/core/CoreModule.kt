package es.iridiobis.temporizador.core

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
}

