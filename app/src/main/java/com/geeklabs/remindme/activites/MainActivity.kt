package com.geeklabs.remindme.activites

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.geeklabs.remindme.R
import com.geeklabs.remindme.adapters.ReminderAdapter
import com.geeklabs.remindme.database.DatabaseHandler
import com.geeklabs.remindme.models.Reminder
import com.geeklabs.remindme.services.ReminderService
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val RC_OVERLAY: Int = 1
    private lateinit var adapter: ReminderAdapter
    private var reminderList = mutableListOf<Reminder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view_reminder.layoutManager = linearLayoutManager

        adapter = ReminderAdapter(reminderList)
        recycler_view_reminder.adapter = adapter

        getAllRemindersFromDB()

        btn.setOnClickListener { view ->
            val reminderIntent = Intent(this, AddReminderActivity::class.java)
            startActivity(reminderIntent)

        }

        val from = intent.getStringExtra("from")
        if (from == "Notification") {
            val reminderServiceIntent = Intent(this, ReminderService::class.java)
            stopService(reminderServiceIntent)
        }
    }

    private fun getAllRemindersFromDB() {
        val databaseHandler = DatabaseHandler(this)
        reminderList = databaseHandler.getAll()
        adapter.reminderList = reminderList
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        getAllRemindersFromDB()
    }

    /*val builder = AlertDialog.Builder(this)
       builder.setTitle(reminder.title)
       builder.setMessage(reminder.description)
       builder.setPositiveButton("STOP ALARM") { dialog, which ->
           mediaPlayer?.stop()
           Util.showToastMessage(this, "Your alarm has been stopped")
           dialog.dismiss()
       }

       val alertDialog = builder.create()
       alertDialog.window?.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
       alertDialog.show()*/
}
