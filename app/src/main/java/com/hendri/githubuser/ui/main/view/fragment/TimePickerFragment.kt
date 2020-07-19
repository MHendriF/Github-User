package com.hendri.githubuser.ui.main.view.fragment

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class TimePickerFragment(
    private val time: LocalTime = LocalTime.now(),
    private val listener: TimePickerListener
) : DialogFragment(),
    TimePickerDialog.OnTimeSetListener {
    class TimePickerListener(val listener: (hour: Int, minute: Int) -> Unit) {
        fun onTimeSet(hour: Int, minute: Int) = listener(hour, minute)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val use24HourFormat = true
        return TimePickerDialog(requireActivity(), this, time.hour, time.minute, use24HourFormat)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        listener.onTimeSet(hourOfDay, minute)
    }
}
