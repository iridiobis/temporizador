package es.iridiobis.temporizador.presentation.ui.finishedtask

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.presentation.services.AlarmMediaService
import es.iridiobis.temporizador.presentation.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_finished_task.*

class FinishedTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_finished_task)
        finish.setOnClickListener {
            stopService(Intent(this, AlarmMediaService::class.java))
            startActivity(
                    Intent(this, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
    }
}
