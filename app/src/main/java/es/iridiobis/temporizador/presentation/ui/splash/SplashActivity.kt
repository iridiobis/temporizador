package es.iridiobis.temporizador.presentation.ui.splash

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.domain.services.AlarmService
import es.iridiobis.temporizador.presentation.ui.finishedtask.FinishedTaskActivity
import es.iridiobis.temporizador.presentation.ui.main.MainActivity
import es.iridiobis.temporizador.presentation.ui.runningtask.RunningTaskActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject lateinit var alarmService: AlarmService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as ComponentProvider<ApplicationComponent>).getComponent().inject(this)
        alarmService.hasRunningTask()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        {
                            if (it && alarmService.hasGoneOff()) {
                                startActivity(Intent(this, FinishedTaskActivity::class.java))
                            } else if (it) {
                                startActivity(Intent(this, RunningTaskActivity::class.java))
                            } else {
                                startActivity(Intent(this, MainActivity::class.java))
                            }
                        }
                )
    }
}
