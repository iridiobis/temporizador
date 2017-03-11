package es.iridiobis.temporizador.core.alarm

import android.content.Context
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import es.iridiobis.temporizador.presentation.services.FireAlarmService
import es.iridiobis.temporizador.presentation.services.AlarmMediaService
import es.iridiobis.temporizador.presentation.ui.main.MainActivity

class AlarmReceiver : WakefulBroadcastReceiver() {

    companion object {
        fun playIntent(context: Context) : Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = AlarmMediaService.ACTION_PLAY
            return intent
        }

        fun stopIntent(context: Context) : Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = AlarmMediaService.ACTION_STOP
            return intent
        }
    }

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
        }
    }
}