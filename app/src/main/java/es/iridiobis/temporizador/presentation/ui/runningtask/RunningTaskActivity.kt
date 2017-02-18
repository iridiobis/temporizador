package es.iridiobis.temporizador.presentation.ui.runningtask

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import es.iridiobis.temporizador.R

class RunningTaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_running_task)
    }
}
