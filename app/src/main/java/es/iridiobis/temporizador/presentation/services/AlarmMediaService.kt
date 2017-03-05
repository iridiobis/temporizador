package es.iridiobis.temporizador.presentation.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.IBinder
import android.os.PowerManager
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.v4.app.NotificationCompat
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.core.alarm.AlarmReceiver
import es.iridiobis.temporizador.data.storage.ImagesStorage
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.presentation.ui.finishedtask.FinishedTaskActivity
import es.iridiobis.temporizador.presentation.ui.runningtask.RunningTaskActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException

class AlarmMediaService : Service(), MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener {

    companion object {
        val ID_NOTIFICATION = 1
        val ACTION_PLAY = "es.iridiobis.temporizador.presentation.services.ACTION_PLAY"
        val ACTION_PAUSE = "es.iridiobis.temporizador.presentation.services.ACTION_PAUSE"
        val ACTION_STOP = "es.iridiobis.temporizador.presentation.services.ACTION_STOP"
    }

    var mediaPlayer: MediaPlayer? = null

    override fun onBind(p0: Intent?): IBinder {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        TasksStorage(ImagesStorage(applicationContext)).retrieveTask(PreferenceManager.getDefaultSharedPreferences(this).getLong("TASK", 0))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    showFinishedNotification(it)
                })
        if (intent?.action.equals(ACTION_PLAY)) {
            initMediaPlayer()
        }
        return super.onStartCommand(intent, flags, startId);
    }

    fun initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        try {
            mediaPlayer!!.setDataSource(this, uri)
        } catch (e: IOException) {
            e.printStackTrace();
        }

        mediaPlayer!!.setOnPreparedListener(this)
        mediaPlayer!!.setOnErrorListener(this)
        mediaPlayer!!.prepareAsync()
    }

    /**
     * Called when the media file is ready for playback.
     * @param mediaPlayer the MediaPlayer that is ready for playback
     */
    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }

    fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    override fun onError(mediaPlayer: MediaPlayer?, what: Int, extra: Int): Boolean {
        stop()
        return true
    }

    private fun showFinishedNotification(it: Task) {

        val pendingSnooze = PendingIntent.getBroadcast (this, 0, AlarmReceiver.stopIntent(this), PendingIntent.FLAG_CANCEL_CURRENT)

        val dismiss = NotificationCompat.Action(
                R.drawable.ic_notifications_off_black_24,
                getString(R.string.done),
                pendingSnooze)

        val content = PendingIntent.getActivity (this, 0, FinishedTaskActivity.newIntent(it.id, this), 0)

        val builder = getBaseNotificationBuilder(it)
                .setContentText(getString(R.string.enough_message))
                .setContentIntent(content)
                .addAction(dismiss)

        startForeground(ID_NOTIFICATION, builder.build())
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