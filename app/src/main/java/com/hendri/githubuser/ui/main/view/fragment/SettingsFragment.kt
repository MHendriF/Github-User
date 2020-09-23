package com.hendri.githubuser.ui.main.view.fragment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.hendri.githubuser.R
import com.hendri.githubuser.utils.reminder.AlarmScheduler
import java.time.LocalTime


class SettingsFragment : PreferenceFragmentCompat() {
    companion object {
        const val TIME_PICKER_REMINDER = "TimePickerReminder"
    }

    private var reminderPref: SwitchPreference? = null
    private var timePref: Preference? = null
    private var localePref: Preference? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)

        reminderPref = findPreference(getString(R.string.reminder_key))
        timePref = findPreference(getString(R.string.reminder_time_key))
        localePref = findPreference(getString(R.string.locale_key))

        setupActionBar()
        setupReminder()
        setupTimeReminder()
        setupLocale()
        setupChannel(
            getString(R.string.github_notification_channel_id),
            getString(R.string.github_notification_channel_name)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupReminder() {
        reminderPref?.setOnPreferenceChangeListener { _, newValue ->
            if (newValue as Boolean) {
                requireContext().toast("reminder on")
                AlarmScheduler.scheduleAlarm(requireContext())
            } else {
                requireContext().toast("reminder off")
                AlarmScheduler.cancelAlarm(requireContext())
            }
            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupTimeReminder() {
        timePref?.let { pref ->
            setupTimer(pref)
            pref.summary = requireContext().getString(
                R.string.reminder_time_summary,
                pref.sharedPreferences.getString(pref.key, "")
            )
            pref.setOnPreferenceClickListener {
                promptTimePickerDialog(it)
                true
            }
        }
    }

    private fun setupLocale() {
        localePref?.setOnPreferenceClickListener {
            Intent(Settings.ACTION_LOCALE_SETTINGS).also { intent ->
                startActivity(intent)
            }
            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun promptTimePickerDialog(pref: Preference) {
        val prefTime = pref.sharedPreferences.getString(pref.key, "")
        TimePickerFragment(
            LocalTime.parse(prefTime),
            TimePickerFragment.TimePickerListener { hour, minute ->
                pref.sharedPreferences.edit {
                    putString(
                        pref.key,
                        LocalTime.of(hour, minute).toString()
                    )
                }
                pref.summary = requireContext().getString(
                    R.string.reminder_time_summary,
                    pref.sharedPreferences.getString(pref.key, "")
                )
                AlarmScheduler.scheduleAlarm(requireContext())
            }).show(childFragmentManager, TIME_PICKER_REMINDER)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupTimer(pref: Preference?) {
        pref?.let {
            val time = pref.sharedPreferences.getString(pref.key, null)
            if (time == null) {
                pref.sharedPreferences.edit {
                    putString(
                        pref.key,
                        LocalTime.of(9, 0).toString()
                    )
                }
            }
        }
    }

    private fun setupChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).
                    apply {
                        setShowBadge(false)
                    }
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.reminder_notification_channel_description)

            val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun setupActionBar() {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.settings)
    }

    private fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}