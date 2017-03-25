package es.iridiobis.temporizador.presentation.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.IBinder
import android.os.PowerManager
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.core.notification.NotificationProvider
import es.iridiobis.temporizador.domain.services.AlarmService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject

class AlarmMediaService : Service(), MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener {

    companion object {
        val ACTION_PLAY = "AlarmMediaService.ACTION_PLAY"
        val ACTION_STOP = "AlarmMediaService.ACTION_STOP"
    }

    @Inject lateinit var alarmService: AlarmService
    @Inject lateinit var notificationProvider: NotificationProvider

    var mediaPlayer: MediaPlayer? = null

    override fun onBind(p0: Intent?): IBinder {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate() {
        super.onCreate()
        (application as ComponentProvider<ApplicationComponent>).getComponent().inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action.equals(ACTION_PLAY)) {
            alarmService.getRunningTask()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        startForeground(notificationProvider.notificationId, notificationProvider.showFinishedNotification(it!!))
                    })
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

}