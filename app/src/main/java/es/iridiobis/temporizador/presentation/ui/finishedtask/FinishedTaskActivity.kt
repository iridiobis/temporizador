package es.iridiobis.temporizador.presentation.ui.finishedtask

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.Temporizador
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.presentation.services.AlarmMediaService
import es.iridiobis.temporizador.presentation.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_finished_task.*
import javax.inject.Inject

class FinishedTaskActivity : AppCompatActivity(), FinishedTask.View {

    companion object {
        fun newIntent(context: Context) = Intent(context, FinishedTaskActivity::class.java)
    }

    @Inject lateinit var presenter: FinishedTask.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_finished_task)
        DaggerFinishedTaskComponent.builder()
                .applicationComponent((application as Temporizador).getComponent())
                .build()
                .injectMembers(this)
        presenter.attach(this)
        ft_stop_fab.setOnClickListener { presenter.finishAlarm() }
    }

    override fun onDestroy() {
        presenter.detach(this)
        super.onPause()
    }

    override fun soundAlarm() {
        val mpIntent = Intent(this, AlarmMediaService::class.java)
        mpIntent.action = AlarmMediaService.ACTION_PLAY
        startService(mpIntent)
    }

    override fun displayBackground(background: Uri) {
        ft_background.setBackground(background) { request -> request }
    }

    override fun silenceAlarm() {
        stopService(Intent(this, AlarmMediaService::class.java))
    }

    override fun goToMainScreen() {
        startActivity(
                Intent(this, MainActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
        finish()
    }
}
