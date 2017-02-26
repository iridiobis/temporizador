package es.iridiobis.temporizador.presentation.dialogs

import mobi.upod.timedurationpicker.TimeDurationPicker
import mobi.upod.timedurationpicker.TimeDurationPickerDialogFragment


class DurationDialogFragment(val initial : Long = 0) : TimeDurationPickerDialogFragment() {

    override fun getInitialDuration(): Long {
        return initial
    }
    override fun onDurationSet(view: TimeDurationPicker?, duration: Long) {
        (activity as DurationDialogListener).onTimeSet(duration)
    }
}

interface DurationDialogListener {
    fun onTimeSet(duration: Long)
}