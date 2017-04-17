package es.iridiobis.temporizador.presentation.dialogs

import android.os.Bundle
import mobi.upod.timedurationpicker.TimeDurationPicker
import mobi.upod.timedurationpicker.TimeDurationPickerDialogFragment


class DurationDialogFragment(var initial : Long = 0) : TimeDurationPickerDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            initial = savedInstanceState.getLong("duration", 0)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("duration", initial)
    }
    override fun getInitialDuration(): Long {
        return initial
    }
    override fun onDurationSet(view: TimeDurationPicker?, duration: Long) {
        if (targetFragment == null) {
            (activity as DurationDialogListener).onTimeSet(duration)
        } else {
            (targetFragment as DurationDialogListener).onTimeSet(duration)
        }
    }
}

interface DurationDialogListener {
    fun onTimeSet(duration: Long)
}