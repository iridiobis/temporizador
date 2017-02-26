package es.iridiobis.temporizador.presentation.ui.main

import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import es.iridiobis.kotlinexample.snack
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.alarm.AlarmHandler
import es.iridiobis.temporizador.data.storage.ImagesStorage
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.presentation.ui.addTask.AddTaskActivity
import es.iridiobis.temporizador.presentation.ui.runningtask.RunningTaskActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), Main.View {

    var presenter : Main.Presenter? = null
    val tasksAdapter = TasksAdapter { startTask(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        presenter = MainPresenter(TasksStorage(ImagesStorage(ContextWrapper(applicationContext))))
        main_tasks.adapter = tasksAdapter

        fab.setOnClickListener { view ->
            startActivity(Intent(this, AddTaskActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        presenter!!.attach(this)
    }

    override fun onPause() {
        presenter!!.detach(this)
        super.onPause()
    }

    override fun displayTasks(tasks: List<Task>) {
        tasksAdapter.data = tasks
    }

    private fun startTask(task: Task) {
        main_tasks.snack("Starting task " + task.name)
        AlarmHandler(this).setAlarm(task)
        startActivity(
                RunningTaskActivity.newIntent(task.id, this)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }
}
