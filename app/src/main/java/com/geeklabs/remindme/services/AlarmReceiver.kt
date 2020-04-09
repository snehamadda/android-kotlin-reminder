package com.geeklabs.remindme.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val reminderId = intent?.getLongExtra("reminderId", 0)

        val reminderServiceIntent = Intent(context, ReminderService::class.java)
        reminderServiceIntent.putExtra("reminderId", reminderId)
        context.startService(reminderServiceIntent)

        /*val intentAlarm = Intent(context, AlarmActivity::class.java)
        intentAlarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intentAlarm.putExtra("reminderId", reminderId)
        context.startActivity(intentAlarm)*/
    }
}