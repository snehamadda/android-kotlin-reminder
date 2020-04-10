package com.geeklabs.remindme.activites

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.geeklabs.remindme.R
import com.geeklabs.remindme.adapters.ReminderAdapter
import com.geeklabs.remindme.database.DatabaseHandler
import com.geeklabs.remindme.models.Reminder
import com.geeklabs.remindme.services.AlarmReceiver
import com.geeklabs.remindme.services.ReminderService
import com.geeklabs.remindme.utils.Util
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var adapter: ReminderAdapter
    private var reminderList = mutableListOf<Reminder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHandler = DatabaseHandler(this)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view_reminder.layoutManager = linearLayoutManager

        adapter = ReminderAdapter(reminderList)
        recycler_view_reminder.adapter = adapter

        getAllRemindersFromDB()

        addReminderButton.setOnClickListener {
            val reminderIntent = Intent(this, AddReminderActivity::class.java)
            startActivity(reminderIntent)

        }

        val from = intent.getStringExtra("from")
        if (from == "Notification") {
            val reminderId = intent.getLongExtra("reminderId", 0)
            val reminderById = databaseHandler.getReminderById(reminderId)
            showReminderAlert(reminderById)
        }
    }

    private fun getAllRemindersFromDB() {
        reminderList = databaseHandler.getAll()
        adapter.reminderList = reminderList
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        getAllRemindersFromDB()
    }

    private fun showReminderAlert(reminder: Reminder) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(reminder.title)
        builder.setMessage(reminder.description)
        builder.setPositiveButton("STOP ALARM") { dialog, _ ->
            Util.showToastMessage(this, "Your alarm has been stopped")
            dialog.dismiss()
            stopAlarm()
            stopReminderService()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    private fun stopReminderService() {
        val reminderService = Intent(this, ReminderService::class.java)
        stopService(reminderService)
    }

    private fun stopAlarm() {
        val intent = Intent(this, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(this, 0, intent, 0)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
}
