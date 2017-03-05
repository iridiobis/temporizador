package es.iridiobis.temporizador.presentation.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Icon
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.IBinder
import android.os.PowerManager
import android.preference.PreferenceManager
import android.support.v4.app.NotificationCompat
import es.iridiobis.temporizador.R
import es.iridiobis.temporizador.data.storage.ImagesStorage
import es.iridiobis.temporizador.data.storage.TasksStorage
import es.iridiobis.temporizador.domain.model.Task
import es.iridiobis.temporizador.presentation.ui.finishedtask.FinishedTaskActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.text.Format

class AlarmMediaService : Service(), MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener {

    companion object {
        val ID_NOTIFICATION = 1
        val ACTION_PLAY = "com.example.adam.nfcalarm.alertservice.ACTION_PLAY"
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
                    showNotification(it)
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

    private fun showNotification(it: Task) {

        val snooze = FinishedTaskActivity.newIntent(it.id, this)
        //snooze.setAction(ACTION_SNOOZE_ALARM);
        val pendingSnooze = PendingIntent.getActivity (this, 0, snooze, PendingIntent.FLAG_ONE_SHOT)

        //val icon = Icon.createWithResource(this, R.drawable.ic_snooze_black_18dp);

        val dismiss = NotificationCompat.Action(R.mipmap.ic_launcher, "Done", pendingSnooze)

        val content = PendingIntent.getActivity (this, 0, FinishedTaskActivity.newIntent(it.id, this), 0)

        val builder = NotificationCompat.Builder(this)
                //.setSmallIcon(R.drawable.icon_nfc)
                .setContentTitle("Active Alarm")
                //.setContentText(millis)
                .setContentIntent(content)
                .setShowWhen(false)
                .setAutoCancel(false)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                //.addAction(dismiss)

        startForeground(ID_NOTIFICATION, builder.build());
    }
}