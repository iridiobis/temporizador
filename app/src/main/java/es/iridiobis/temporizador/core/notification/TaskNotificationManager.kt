package es.iridiobis.temporizador.core.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.provider.MediaStore
import android.support.v4.app.NotificationCompat
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.alarm.AlarmReceiver
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.domain.services.TaskNotification
import es.iridiobis.temporizador.presentation.ui.finishedtask.FinishedTaskActivity
import es.iridiobis.temporizador.presentation.ui.runningtask.RunningTaskActivity
import javax.inject.Inject

class TaskNotificationManager @Inject constructor(
        val context: Context,
        val notificationManager: NotificationManager) : TaskNotification {

    companion object {
        private val NOTIFICATION_ID = 1
    }

    override fun showRunningNotification(it: Task) {

        val pendingPause = PendingIntent.getBroadcast(context, 0, AlarmReceiver.pauseTaskIntent(context), PendingIntent.FLAG_CANCEL_CURRENT)
        val pause = NotificationCompat.Action(
                R.drawable.ic_notifications_off_black_24,
                context.getString(R.string.pause),
                pendingPause)

        val content = PendingIntent.getActivity(context, 0, RunningTaskActivity.newIntent(context), 0)

        val notification = getBaseNotificationBuilder(it)
                .setContentText("Keep working")
                .setContentIntent(content)
                .addAction(pause)
                .addAction(getStopAction())
                .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun showPausedNotification(it: Task) {

        val pendingPause = PendingIntent.getBroadcast(context, 0, AlarmReceiver.resumeTaskIntent(context), PendingIntent.FLAG_CANCEL_CURRENT)

        val pause = NotificationCompat.Action(
                R.drawable.ic_notifications_off_black_24,
                context.getString(R.string.resume),
                pendingPause)

        val content = PendingIntent.getActivity(context, 0, RunningTaskActivity.newIntent(context), 0)

        val notification = getBaseNotificationBuilder(it)
                .setContentText("Keep working")
                .setContentIntent(content)
                .addAction(pause)
                .addAction(getStopAction())
                .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun showFinishedNotification(it: Task, service: Service) {

        val pendingSnooze = PendingIntent.getBroadcast(context, 0, AlarmReceiver.stopAlarmIntent(context), PendingIntent.FLAG_CANCEL_CURRENT)

        val dismiss = NotificationCompat.Action(
                R.drawable.ic_notifications_off_black_24,
                context.getString(R.string.done),
                pendingSnooze)

        val content = PendingIntent.getActivity(context, 0, FinishedTaskActivity.newIntent(it.id, context), 0)

        val notification = getBaseNotificationBuilder(it)
                .setContentText(context.getString(R.string.enough_message))
                .setContentIntent(content)
                .addAction(dismiss)
                .build()

        service.startForeground(NOTIFICATION_ID, notification)
    }

    override fun cancel() {
        notificationManager.cancel(NOTIFICATION_ID)
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

    private fun getStopAction() : NotificationCompat.Action {
        val pendingStop = PendingIntent.getBroadcast(context, 0, AlarmReceiver.stopTaskIntent(context), PendingIntent.FLAG_CANCEL_CURRENT)
        //TODO icon
        return NotificationCompat.Action(
                R.drawable.ic_notifications_off_black_24,
                context.getString(R.string.stop),
                pendingStop)
    }

}