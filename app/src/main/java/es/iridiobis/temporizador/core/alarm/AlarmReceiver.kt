package es.iridiobis.temporizador.core.alarm

import android.content.Context
import android.content.Intent
import android.support.v4.content.WakefulBroadcastReceiver
import es.iridiobis.temporizador.core.Temporizador
import es.iridiobis.temporizador.domain.services.AlarmService
import javax.inject.Inject

class AlarmReceiver : WakefulBroadcastReceiver() {

    companion object {
        private val ACTION_PAUSE_TASK = "AlarmReceiver.ACTION_PAUSE_TASK"
        private val ACTION_RESUME_TASK = "AlarmReceiver.ACTION_RESUME_TASK"
        private val ACTION_STOP_TASK = "AlarmReceiver.ACTION_STOP_TASK"

        private val ACTION_PLAY_ALARM = "AlarmReceiver.ACTION_PLAY_ALARM"
        private val ACTION_STOP_ALARM = "AlarmReceiver.ACTION_STOP_ALARM"

        fun pauseTaskIntent(context: Context) : Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = ACTION_PAUSE_TASK
            return intent
        }

        fun resumeTaskIntent(context: Context) : Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = ACTION_RESUME_TASK
            return intent
        }

        fun stopTaskIntent(context: Context) : Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = ACTION_STOP_TASK
            return intent
        }

        fun playAlarmIntent(context: Context) : Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = ACTION_PLAY_ALARM
            return intent
        }

        fun stopAlarmIntent(context: Context) : Intent {
            val intent = Intent(context, AlarmReceiver::class.java)
            intent.action = ACTION_STOP_ALARM
            return intent
        }
    }

    @Inject lateinit var alarmService: AlarmService

    override fun onReceive(context: Context, intent: Intent) {
        (context.applicationContext as Temporizador).getComponent().inject(this)
        if (ACTION_PLAY_ALARM == intent.action) {
            alarmService.playAlarm()
        } else if (ACTION_STOP_ALARM == intent.action) {
            alarmService.stopAlarm()
        } else if (ACTION_PAUSE_TASK == intent.action) {
            alarmService.pauseTask()
        } else if (ACTION_RESUME_TASK == intent.action) {
            alarmService.resumeTask()
        } else if (ACTION_STOP_TASK == intent.action) {
            alarmService.stopTask()
        }
    }
}