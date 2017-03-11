package es.iridiobis.temporizador.core.alarm

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.v4.app.NotificationCompat
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.services.AlarmService
import es.iridiobis.temporizador.presentation.services.AlarmMediaService
import es.iridiobis.temporizador.presentation.ui.runningtask.RunningTaskActivity
import io.reactivex.Observable
import javax.inject.Inject

class AlarmHandler @Inject constructor(val tasksStorage: TasksStorage, val context : Context) : AlarmService {
    var task : Task? = null
    override fun getRunningTask(): Observable<Task?> {
        if (task != null) {
            return Observable.just(task)
        } else {
            return tasksStorage.retrieveTask(PreferenceManager.getDefaultSharedPreferences(context).getLong("TASK", 0))
        }
    }

    override fun setAlarm(task: Task) {
        this.task = task
        PreferenceManager.getDefaultSharedPreferences(context).edit()
                .putLong("TASK", task.id)
                .putLong("START_TIME", System.currentTimeMillis())
                .apply()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = PendingIntent.getBroadcast(context, 0, AlarmReceiver.playIntent(context), 0)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + task.duration, alarmIntent)
        else
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + task.duration, alarmIntent)
        showRunningNotification(task)
    }

    private fun showRunningNotification(it: Task) {

        val pendingPause = PendingIntent.getBroadcast(context, 0, AlarmReceiver.stopIntent(context), PendingIntent.FLAG_CANCEL_CURRENT)

        val pause = NotificationCompat.Action(
                R.drawable.ic_notifications_off_black_24,
                context.getString(R.string.pause),
                pendingPause)

        val content = PendingIntent.getActivity(context, 0, RunningTaskActivity.newIntent(it.id, context), 0)

        val builder = getBaseNotificationBuilder(it)
                .setContentText("Keep working")
                .setContentIntent(content)
                .addAction(pause)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(AlarmMediaService.ID_NOTIFICATION, builder.build())
    }

    private fun getBaseNotificationBuilder(it: Task): NotificationCompat.Builder {
        return NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_task_add_white_24)
                .setLargeIcon(MediaStore.Images.Media.getBitmap(context.contentResolver, it.thumbnail))
                .setContentTitle(it.name)
                .setShowWhen(false)
                .setAutoCancel(false)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
    }

}