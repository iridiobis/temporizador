package es.iridiobis.temporizador.presentation.ui.runningtask

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.presentation.ui.main.DaggerRunningTaskComponent
import kotlinx.android.synthetic.main.activity_running_task.*
import javax.inject.Inject

class RunningTaskActivity : AppCompatActivity(), RunningTask.View {

    companion object {
        fun newIntent(id : Long, context: Context) : Intent {
            var intent = Intent(context, RunningTaskActivity::class.java)
            intent.putExtra("TASK", id)
            return intent
        }
    }

    @Inject lateinit var presenter: RunningTask.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running_task)
        DaggerRunningTaskComponent.builder()
                .applicationComponent((application as ComponentProvider<ApplicationComponent>).getComponent())
                .build()
                .injectMembers(this)
    }

    override fun onResume() {
        super.onResume()
        presenter!!.attach(this)
    }

    override fun onPause() {
        presenter!!.detach(this)
        super.onPause()
    }
    override fun displayName(name: String) {
        supportActionBar?.title = name
    }

    override fun displayBackground(background: Uri) {
        activity_running_task.setBackground(background) { request -> request }
    }

    override fun displayStatus(status: Boolean) {
        rt_status.text = if(status) "Running" else "Paused"
    }

}
