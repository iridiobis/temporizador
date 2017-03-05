package es.iridiobis.temporizador.presentation.ui.main

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.alarm.AlarmHandler
import es.iridiobis.temporizador.core.alarm.AlarmReceiver
import es.iridiobis.temporizador.data.storage.ImagesStorage
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.presentation.services.AlarmMediaService
import es.iridiobis.temporizador.presentation.ui.writetask.WriteTaskActivity
import es.iridiobis.temporizador.presentation.ui.runningtask.RunningTaskActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), Main.View {

    var presenter : Main.Presenter? = null
    val tasksAdapter = TasksAdapter(
            { startTask(it) },
            { startActivity(WriteTaskActivity.editTaskIntent(it.id, this)) },
            { requestDeleteConfirmation(it) })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        presenter = MainPresenter(TasksStorage(ImagesStorage(ContextWrapper(applicationContext))))
        main_tasks.adapter = tasksAdapter

        fab.setOnClickListener { startActivity(WriteTaskActivity.addTaskIntent(this)) }
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
        //TODO move to presenter/repo
        PreferenceManager.getDefaultSharedPreferences(this).edit().putLong("TASK", task.id).apply()
        AlarmHandler(this).setAlarm(task)
        showRunningNotification(task)
        startActivity(
                RunningTaskActivity.newIntent(task.id, this)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    private fun requestDeleteConfirmation(task: Task) {
        AlertDialog.Builder(this)
                .setTitle(R.string.delete_alert_title)
                .setPositiveButton(R.string.delete, { _, _ -> presenter?.delete(task)})
                .setNegativeButton(R.string.cancel, { _, _ -> })
                .create()
                .show()
    }


    private fun showRunningNotification(it: Task) {

        val pendingPause = PendingIntent.getBroadcast (this, 0, AlarmReceiver.stopIntent(this), PendingIntent.FLAG_CANCEL_CURRENT)

        val pause = NotificationCompat.Action(
                R.drawable.ic_notifications_off_black_24,
                getString(R.string.pause),
                pendingPause)

        val content = PendingIntent.getActivity (this, 0, RunningTaskActivity.newIntent(it.id, this), 0)

        val builder = getBaseNotificationBuilder(it)
                .setContentText("Keep working")
                .setContentIntent(content)
                .addAction(pause)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(AlarmMediaService.ID_NOTIFICATION, builder.build())
    }

    private fun getBaseNotificationBuilder(it: Task) : NotificationCompat.Builder {
        return NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_task_add_white_24)
                .setLargeIcon(MediaStore.Images.Media.getBitmap(contentResolver, it.thumbnail))
                .setContentTitle(it.name)
                .setShowWhen(false)
                .setAutoCancel(false)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
    }
}
