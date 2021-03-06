package es.iridiobis.temporizador.core

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import es.iridiobis.temporizador.core.alarm.AlarmReceiver
import es.iridiobis.temporizador.data.DataModule
import es.iridiobis.temporizador.domain.repositories.StringsRepository
import es.iridiobis.temporizador.domain.repositories.TasksRepository
import es.iridiobis.temporizador.domain.services.TaskService
import es.iridiobis.temporizador.presentation.services.AlarmMediaService
import es.iridiobis.temporizador.presentation.services.FireAlarmService
import es.iridiobis.temporizador.presentation.ui.splash.SplashActivity
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(DataModule::class, CoreModule::class))
interface ApplicationComponent {

    fun inject(temporizador: Temporizador)
    fun inject(fireAlarmService: FireAlarmService)
    fun inject(alarmMediaService: AlarmMediaService)
    fun inject(alarmReceiver: AlarmReceiver)
    fun inject(splashActivity: SplashActivity)

    fun provideTasksRepository() : TasksRepository
    fun provideTaskService() : TaskService
    fun provideStringsRepository() : StringsRepository

    @Component.Builder
    interface Builder {
        @BindsInstance fun context(context: Context): Builder
        fun dataModule(dataModule: DataModule) : Builder
        fun build(): ApplicationComponent
    }
}