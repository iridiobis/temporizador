package es.iridiobis.temporizador.presentation.ui.runningtask

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.View.GONE
import android.view.View.VISIBLE
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.Temporizador
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.presentation.ui.finishedtask.FinishedTaskActivity
import es.iridiobis.temporizador.presentation.ui.main.DaggerRunningTaskComponent
import es.iridiobis.temporizador.presentation.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_running_task.*
import javax.inject.Inject

class RunningTaskActivity : AppCompatActivity(), RunningTask.View {

    companion object {
        fun newIntent(context: Context) = Intent(context, RunningTaskActivity::class.java)
    }

    @Inject lateinit var presenter: RunningTask.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running_task)
        DaggerRunningTaskComponent.builder()
                .applicationComponent((application as Temporizador).getComponent())
                .build()
                .injectMembers(this)
        rt_status_fab.setOnClickListener { presenter.changeStatus() }
        rt_stop.setOnClickListener { requestStopConfirmation() }
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        presenter.detach(this)
        super.onPause()
    }
    override fun displayName(name: String) {
        supportActionBar?.title = name
    }

    override fun displayBackground(background: Uri) {
        activity_running_task.setBackground(background) { request -> request }
    }

    override fun displayStatus(status: Boolean) {
        rt_status_fab.setImageDrawable(ContextCompat.getDrawable(this,
                if (status) R.drawable.ic_pause else R.drawable.ic_play_arrow_black_24dp)
        )
    }

    override fun onTaskStopped() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onAlarmGoneOff() {
        startActivity(Intent(this, FinishedTaskActivity::class.java))
        finish()
    }

    private fun requestStopConfirmation() {
        AlertDialog.Builder(this)
                .setTitle(R.string.stop_alert_title)
                .setPositiveButton(R.string.yes, { _, _ -> presenter.stop() })
                .setNegativeButton(R.string.no, { _, _ -> })
                .create()
                .show()
    }

}
