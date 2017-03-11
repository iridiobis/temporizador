package es.iridiobis.temporizador.core

import android.app.Activity
import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.MembersInjector
import es.iridiobis.temporizador.data.DataModule
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import es.iridiobis.temporizador.presentation.services.AlarmMediaService
import es.iridiobis.temporizador.presentation.services.FireAlarmService
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DataModule::class, CoreModule::class))
interface ApplicationComponent {

    fun inject(temporizador: Temporizador)
    fun inject(fireAlarmService: FireAlarmService)
    fun inject(alarmMediaService: AlarmMediaService)

    fun tasksRepository() : TasksRepository

    @Component.Builder
    interface Builder {
        @BindsInstance fun context(context: Context): Builder
        fun dataModule(dataModule: DataModule) : Builder
        fun build(): ApplicationComponent
    }
}