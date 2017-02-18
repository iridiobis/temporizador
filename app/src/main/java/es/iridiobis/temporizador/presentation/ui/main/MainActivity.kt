package es.iridiobis.temporizador.presentation.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import es.iridiobis.kotlinexample.TasksAdapter
import es.iridiobis.kotlinexample.snack
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.data.storage.ImagesStorage
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.model.Task
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private fun startTask(task: Task) {
        //TODO
        main_tasks.snack("Starting task " + task.name)
    }

    val tasksAdapter = TasksAdapter(ImagesStorage()) { startTask(it) }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        main_tasks.adapter = tasksAdapter
        //TODO mvp, injection
        TasksStorage().retrieveTasks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ tasksAdapter.data = it })

        fab.setOnClickListener { view ->
            view.snack("Replace with your own action")
        }
    }
}
