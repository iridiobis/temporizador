package es.iridiobis.temporizador.presentation.ui.finishedtask

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.extensions.setBackground
import es.iridiobis.temporizador.data.storage.ImagesStorage
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.presentation.services.AlarmMediaService
import es.iridiobis.temporizador.presentation.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_finished_task.*

class FinishedTaskActivity : AppCompatActivity(), FinishedTask.View {

    companion object {
        fun newIntent(id : Long, context: Context) : Intent {
            var intent = Intent(context, FinishedTaskActivity::class.java)
            intent.putExtra("TASK", id)
            return intent
        }
    }
    var presenter: FinishedTask.Presenter? = null

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
        presenter = FinishedTaskPresenter(intent.extras.getLong("TASK"), TasksStorage(ImagesStorage(applicationContext)))
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
        activity_finished_task.setBackground(background) { request -> request }
    }
}
