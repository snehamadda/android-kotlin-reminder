package com.geeklabs.remindme.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import com.geeklabs.remindme.R
import com.geeklabs.remindme.utils.Util

class ReminderAlarmBroadcastReceiver : BroadcastReceiver() {

    private var mp: MediaPlayer? = null

    override fun onReceive(context: Context, intent: Intent?) {
        Log.d("MyReminderAlarm", "Alarm just fired")
        Util.showToastMessage(context, "Alarm just fried")
        mp = MediaPlayer.create(context, R.raw.alarm_ringtone)
        mp?.start()
    }

}