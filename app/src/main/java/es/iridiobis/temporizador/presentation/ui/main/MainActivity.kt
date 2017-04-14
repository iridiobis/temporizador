package es.iridiobis.temporizador.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.Temporizador
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.presentation.ui.runningtask.RunningTaskActivity
import es.iridiobis.temporizador.presentation.ui.writetask.WriteTaskActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), Main.View {

    @Inject lateinit var presenter: Main.Presenter

    val tasksAdapter = TasksAdapter(
            { startTask(it) },
            { startActivity(WriteTaskActivity.editTaskIntent(it.id, this)) },
            { requestDeleteConfirmation(it) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        DaggerMainComponent.builder()
                .applicationComponent((application as Temporizador).getComponent())
                .build()
                .injectMembers(this)
        main_tasks.adapter = tasksAdapter

        fab.setOnClickListener { startActivity(WriteTaskActivity.addTaskIntent(this)) }
    }

    override fun onResume() {
        super.onResume()
        presenter.attach(this)
    }

    override fun onPause() {
        presenter.detach(this)
        super.onPause()
    }

    override fun displayTasks(tasks: List<Task>) {
        tasksAdapter.data = tasks
    }

    private fun startTask(task: Task) {
        presenter.runTask(task)
        startActivity(
                RunningTaskActivity.newIntent(this)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    private fun requestDeleteConfirmation(task: Task) {
        AlertDialog.Builder(this)
                .setTitle(R.string.delete_alert_title)
                .setPositiveButton(R.string.delete, { _, _ -> presenter.delete(task) })
                .setNegativeButton(R.string.cancel, { _, _ -> })
                .create()
                .show()
    }

}
