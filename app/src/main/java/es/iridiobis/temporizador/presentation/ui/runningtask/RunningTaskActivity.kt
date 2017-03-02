package es.iridiobis.temporizador.presentation.ui.runningtask

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.data.storage.ImagesStorage
import es.iridiobis.temporizador.data.storage.TasksStorage
import kotlinx.android.synthetic.main.activity_running_task.*

class RunningTaskActivity : AppCompatActivity(), RunningTask.View {

    companion object {
        fun newIntent(id : Long, context: Context) : Intent {
            var intent = Intent(context, RunningTaskActivity::class.java)
            intent.putExtra("TASK", id)
            return intent
        }
    }
    var presenter: RunningTask.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_running_task)
        presenter = RunningTaskPresenter(intent.extras.getLong("TASK"), TasksStorage(ImagesStorage(applicationContext)))
    }

    override fun onResume() {
        super.onResume()
        presenter!!.attach(this)
    }

    override fun onPause() {
        presenter!!.detach(this)
        super.onPause()
    }

    override fun displayBackground(background: Uri) {
        activity_running_task.setBackground(background) { request -> request }
    }
}
