package es.iridiobis.temporizador.core.notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.provider.MediaStore
import android.support.v4.app.NotificationCompat
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.alarm.AlarmReceiver
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.presentation.ui.finishedtask.FinishedTaskActivity
import es.iridiobis.temporizador.presentation.ui.runningtask.RunningTaskActivity
import javax.inject.Inject

class NotificationProvider @Inject constructor(val context : Context) {

    val notificationId = 1

    fun showRunningNotification(it: Task) : Notification {

        val pendingPause = PendingIntent.getBroadcast(context, 0, AlarmReceiver.pauseTaskIntent(context), PendingIntent.FLAG_CANCEL_CURRENT)

        val pause = NotificationCompat.Action(
                R.drawable.ic_notifications_off_black_24,
                context.getString(R.string.pause),
                pendingPause)

        val content = PendingIntent.getActivity(context, 0, RunningTaskActivity.newIntent(it.id, context), 0)

        return getBaseNotificationBuilder(it)
                .setContentText("Keep working")
                .setContentIntent(content)
                .addAction(pause)
                .build()
    }

    fun showPausedNotification(it: Task) : Notification {

        val pendingPause = PendingIntent.getBroadcast(context, 0, AlarmReceiver.resumeTaskIntent(context), PendingIntent.FLAG_CANCEL_CURRENT)

        val pause = NotificationCompat.Action(
                R.drawable.ic_notifications_off_black_24,
                context.getString(R.string.resume),
                pendingPause)

        val content = PendingIntent.getActivity(context, 0, RunningTaskActivity.newIntent(it.id, context), 0)

        return getBaseNotificationBuilder(it)
                .setContentText("Keep working")
                .setContentIntent(content)
                .addAction(pause)
                .build()
    }


    fun showFinishedNotification(it: Task) : Notification {

        val pendingSnooze = PendingIntent.getBroadcast (context, 0, AlarmReceiver.stopAlarmIntent(context), PendingIntent.FLAG_CANCEL_CURRENT)

        val dismiss = NotificationCompat.Action(
                R.drawable.ic_notifications_off_black_24,
                context.getString(R.string.done),
                pendingSnooze)

        val content = PendingIntent.getActivity (context, 0, FinishedTaskActivity.newIntent(it.id, context), 0)

        return getBaseNotificationBuilder(it)
                .setContentText(context.getString(R.string.enough_message))
                .setContentIntent(content)
                .addAction(dismiss)
                .build()
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