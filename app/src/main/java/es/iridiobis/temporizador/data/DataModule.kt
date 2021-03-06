package es.iridiobis.temporizador.data

import dagger.Module
import dagger.Provides
import es.iridiobis.temporizador.data.storage.StringsProvider
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.repositories.StringsRepository
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import javax.inject.Singleton

@Module class DataModule {
    @Singleton @Provides
    fun provideStringsRepository(provider: StringsProvider) : StringsRepository = provider

    @Singleton @Provides
    fun provideTasksRepository(proxy: TasksStorage) : TasksRepository = proxy
}

