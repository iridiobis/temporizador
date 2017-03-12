package es.iridiobis.temporizador.core.alarm

import android.content.Context
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import es.iridiobis.temporizador.core.ApplicationComponent
import es.iridiobis.temporizador.core.di.ComponentProvider
import es.iridiobis.temporizador.domain.services.AlarmService
import es.iridiobis.temporizador.presentation.services.FireAlarmService
import es.iridiobis.temporizador.presentation.services.AlarmMediaService
import es.iridiobis.temporizador.presentation.ui.main.MainActivity
import javax.inject.Inject

class AlarmReceiver : WakefulBroadcastReceiver() {

    companion object {
        private val ACTION_PLAY = "es.iridiobis.temporizador.presentation.services.ACTION_PLAY"
        private val ACTION_PAUSE = "es.iridiobis.temporizador.presentation.services.ACTION_PAUSE"
        private val ACTION_RESUME = "es.iridiobis.temporizador.presentation.services.ACTION_RESUME"
        private val ACTION_STOP = "es.iridiobis.temporizador.presentation.services.ACTION_STOP"

        fun playIntent(context: Context) : Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = ACTION_PLAY
            return intent
        }

        fun pauseIntent(context: Context) : Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = ACTION_PAUSE
            return intent
        }

        fun resumeIntent(context: Context) : Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = ACTION_RESUME
            return intent
        }

        fun stopIntent(context: Context) : Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = ACTION_STOP
            return intent
        }
    }

    @Inject lateinit var alarmService: AlarmService

    override fun onReceive(context: Context?, intent: Intent?) {
        if (AlarmMediaService.ACTION_PLAY == intent?.action) {
            val mpIntent = Intent(context, AlarmMediaService::class.java)
            mpIntent.action = AlarmMediaService.ACTION_PLAY
            context?.startService(mpIntent)

            val service = Intent(context, FireAlarmService::class.java)
            WakefulBroadcastReceiver.startWakefulService(context, service)
        } else if (AlarmMediaService.ACTION_STOP == intent?.action) {
            context?.stopService(Intent(context, AlarmMediaService::class.java))
            context?.startActivity(
                    Intent(context, MainActivity::class.java)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        } else if (AlarmMediaService.ACTION_PAUSE == intent?.action) {
            (context?.applicationContext as ComponentProvider<ApplicationComponent>).getComponent().inject(this)
            alarmService.pauseAlarm()
        }
    }
}