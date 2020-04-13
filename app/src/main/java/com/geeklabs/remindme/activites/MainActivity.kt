package com.geeklabs.remindme.activites

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.PopupMenu
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


class MainActivity : AppCompatActivity(), ReminderAdapter.OnItemClickListener, TextWatcher {

    private var filteredListFinal: List<Reminder> = mutableListOf()
    private lateinit var databaseHandler: DatabaseHandler
    private lateinit var adapter: ReminderAdapter
    private var reminderList = mutableListOf<Reminder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHandler = DatabaseHandler(this)

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view_reminder.layoutManager = linearLayoutManager

        adapter = ReminderAdapter(this)
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

        searchET.addTextChangedListener(this)
    }

    @SuppressLint("DefaultLocale")
    override fun afterTextChanged(p0: Editable?) {
        val filterList = mutableListOf<Reminder>()
        filterList.addAll(reminderList)
        if (!p0.isNullOrEmpty()) {
            filteredListFinal =
                filterList.filter {
                    it.title.toLowerCase().contains(p0.toString().toLowerCase()) ||
                            it.description.toLowerCase().contains(p0.toString().toLowerCase())
                }
        } else {
            filteredListFinal = reminderList
        }
        adapter.reminderList = filteredListFinal as MutableList<Reminder>
        adapter.notifyDataSetChanged()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    private fun getAllRemindersFromDB() {
        reminderList = databaseHandler.getAll()
        adapter.reminderList = reminderList
        adapter.notifyDataSetChanged()

        if (reminderList.size > 0) {
            searchET.visibility = View.VISIBLE
        } else {
            searchET.visibility = View.GONE
        }
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

    override fun onItemClick(
        reminder: Reminder,
        view: View,
        position: Int
    ) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            if (it.title == getString(R.string.update)) {
                startActivity(
                    Intent(this, AddReminderActivity::class.java)
                        .putExtra("reminder", reminder)
                )
            } else if (it.title == getString(R.string.delete)) {
                databaseHandler.deleteReminderById(reminder.id)
                adapter.notifyItemRemoved(position)
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

}
