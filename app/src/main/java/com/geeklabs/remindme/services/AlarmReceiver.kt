package com.geeklabs.remindme.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val reminderId = intent?.getLongExtra("reminderId", 0)
        val isServiceRunning = intent?.getBooleanExtra("isServiceRunning", false)

        val reminderServiceIntent = Intent(context, ReminderService::class.java)
        reminderServiceIntent.putExtra("reminderId", reminderId)
        if (!isServiceRunning!!) {
            context.startService(reminderServiceIntent)
        }
    }
}