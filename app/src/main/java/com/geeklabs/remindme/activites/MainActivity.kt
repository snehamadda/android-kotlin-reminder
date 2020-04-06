package com.geeklabs.remindme.activites

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.geeklabs.remindme.R
import com.geeklabs.remindme.adapters.ReminderAdapter
import com.geeklabs.remindme.database.DatabaseHandler
import com.geeklabs.remindme.models.Reminder
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ReminderAdapter
    private var reminderList = mutableListOf<Reminder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view_reminder.layoutManager = linearLayoutManager

        adapter = ReminderAdapter(reminderList)
        recycler_view_reminder.adapter = adapter

        getAllRemindersFromDB()

        btn.setOnClickListener { view ->
            val reminderIntent = Intent(this, AddReminderActivity::class.java)
            startActivity(reminderIntent)

        }
    }

    private fun getAllRemindersFromDB() {
        val databaseHandler = DatabaseHandler(this)
        reminderList = databaseHandler.getAll()
        adapter.reminderList = reminderList
        adapter.notifyDataSetChanged()
    }

    fun showSnackbar(message: String) {
        Snackbar.make(toolbar, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
    }

    override fun onResume() {
        super.onResume()
        getAllRemindersFromDB()
    }
}
