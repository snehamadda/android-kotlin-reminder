package com.geeklabs.remindme.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.geeklabs.remindme.R
import com.geeklabs.remindme.activites.MainActivity
import com.geeklabs.remindme.database.DatabaseHandler
import com.geeklabs.remindme.models.Reminder


class ReminderService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("ReminderService", "onCreate called")
        mediaPlayer = MediaPlayer.create(this, com.geeklabs.remindme.R.raw.alarm_ringtone)
        mediaPlayer?.isLooping = true
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("ReminderService", "onBind called")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ReminderService", "onStart called")
        mediaPlayer?.start()

        val reminderId = intent?.getLongExtra("reminderId", 0)
        val databaseHandler = DatabaseHandler(this)
        val reminder = databaseHandler.getReminderById(reminderId ?: 0)
        showAlarmNotification(reminder)

        return START_NOT_STICKY
    }

    private fun showAlarmNotification(reminder: Reminder) {

        Log.d("ReminderService", "showAlarmNotification called")

        createNotificationChannel(reminder.id)
        // build notification
        val builder = NotificationCompat.Builder(this, reminder.id.toString())
            .setSmallIcon(R.drawable.remind_me_logo) //set icon for notification
            .setContentTitle(reminder.title) //set title of notification
            .setContentText(reminder.description)//this is notification message
            .setAutoCancel(true) // makes auto cancel of notification
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) //set priority of notification

        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //notification message will get at NotificationView
        notificationIntent.putExtra("from", "Notification")

        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(pendingIntent)
        val notification = builder.build()

        // Add as notification
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(reminder.id, notification)

    }

    private fun createNotificationChannel(id: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                id.toString(),
                "Reminder Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    override fun onDestroy() {
        Log.d("ReminderService", "onDestroy called")
        mediaPlayer?.stop()
        mediaPlayer?.release()
        super.onDestroy()
    }
}
