package com.geeklabs.remindme.activites

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.geeklabs.remindme.R
import com.geeklabs.remindme.database.DatabaseHandler
import com.geeklabs.remindme.models.Reminder
import com.geeklabs.remindme.utils.Util

class AlarmActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val reminderId = intent?.getLongExtra("reminderId", 0)
        val databaseHandler = DatabaseHandler(this)
        val reminder = databaseHandler.getReminderById(reminderId ?: 0)

        showAlarmAlert(reminder)

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm_ringtone)
        mediaPlayer?.start()
    }

    private fun showAlarmAlert(reminder: Reminder) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(reminder.title)
        builder.setMessage(reminder.description)
        builder.setPositiveButton("STOP ALARM") { dialog, which ->
            mediaPlayer?.stop()
            Util.showToastMessage(this, "Your alarm has been stopped")
            dialog.dismiss()
            finish()
        }
        val alertDialog = builder.create()
        alertDialog.show()

    }

    override fun onDestroy() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        super.onDestroy()
    }

}
