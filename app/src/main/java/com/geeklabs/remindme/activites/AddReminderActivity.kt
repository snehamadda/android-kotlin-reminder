package com.geeklabs.remindme.activites

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.geeklabs.remindme.R
import com.geeklabs.remindme.database.DatabaseHandler
import com.geeklabs.remindme.models.Reminder
import com.geeklabs.remindme.services.AlarmReceiver
import com.geeklabs.remindme.services.ReminderService
import com.geeklabs.remindme.utils.Util
import kotlinx.android.synthetic.main.activity_add_reminder.*
import java.util.*


@Suppress("CAST_NEVER_SUCCEEDS")
class AddReminderActivity : AppCompatActivity() {

    private lateinit var alarmManager: AlarmManager
    private lateinit var databaseHandler: DatabaseHandler
    private val myCalendar = Calendar.getInstance()
    private var date: DatePickerDialog.OnDateSetListener? = null

    private var hour: Int = 0
    private var minute: Int = 0
    private var reminderSaved = Reminder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        databaseHandler = DatabaseHandler(this)

        if (intent.hasExtra("reminder")) {
            reminderSaved = intent.getSerializableExtra("reminder") as Reminder
        }

        date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate()
        }

        if (reminderSaved.id != 0) {
            titleET.setText(reminderSaved.title)
            descriptionET.setText(reminderSaved.description)
            dateTV.text = reminderSaved.date
            timeTV.text = reminderSaved.time

            saveBtn.text = getString(R.string.update)
        } else {
            updateDate()
            saveBtn.text = getString(R.string.save)
        }

        selectDateButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this, date, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = myCalendar.timeInMillis
            datePickerDialog.show()

        }

        selectTimeButton.setOnClickListener {
            hour = myCalendar.get(Calendar.HOUR_OF_DAY)
            minute = myCalendar.get(Calendar.MINUTE)

            val timePickerDialog =
                TimePickerDialog(
                    this,
                    TimePickerDialog.OnTimeSetListener(function = { _, hour, minute ->
                        myCalendar.set(Calendar.HOUR_OF_DAY, hour)
                        myCalendar.set(Calendar.MINUTE, minute)
                        myCalendar.set(Calendar.SECOND, 0)
                        updateTime(hour, minute)
                    }), hour, minute, true
                )

            timePickerDialog.show()
        }

        saveBtn.setOnClickListener {
            if (titleET.text.isEmpty()) {
                Util.showToastMessage(this, "Please select title")
            } else if (timeTV.text == getString(R.string.time)) {
                Util.showToastMessage(this, "Please select time")
            } else {
                val title = titleET.text.toString()
                val description = descriptionET.text.toString()
                val time = timeTV.text.toString()
                val date = dateTV.text.toString()

                val reminder = Reminder()

                reminder.title = title
                reminder.description = description
                reminder.time = time
                reminder.date = date

                val saveReminderId: Int
                saveReminderId = if (reminderSaved.id != 0) {
                    reminder.id = reminderSaved.id
                    databaseHandler.updateReminder(reminder)
                    reminderSaved.id
                } else {
                    databaseHandler.saveReminder(reminder) as Int
                }
                if (saveReminderId != 0) {
                    Util.showToastMessage(this, "Reminder save/updated successfully")

                    Log.d("AlarmTime", "Hour $hour")
                    Log.d("AlarmTime", "Min $minute")

                    setRemainderAlarm(saveReminderId as Long)

                    finish()
                } else {
                    Util.showToastMessage(this, "Failed to save remainder")
                }


            }
        }
    }


    private fun updateDate() {
        val formattedDate = Util.getFormattedDate(myCalendar.timeInMillis, "dd/MM/YYYY")
        dateTV.text = formattedDate
    }

    @SuppressLint("SetTextI18n")
    private fun updateTime(hour: Int, minute: Int) {
        this.hour = hour
        this.minute = minute
        timeTV.text = "$hour : $minute"
    }

    private fun setRemainderAlarm(savedReminderId: Long) {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val reminderService = ReminderService()
        val reminderReceiverIntent = Intent(this, AlarmReceiver::class.java)

        reminderReceiverIntent.putExtra("reminderId", savedReminderId)
        reminderReceiverIntent.putExtra("isServiceRunning", isServiceRunning(reminderService))
        val pendingIntent =
            PendingIntent.getBroadcast(this, savedReminderId.toInt(), reminderReceiverIntent, 0)
        val formattedDate = Util.getFormattedDate(myCalendar.timeInMillis, "dd/MM/YYYY HH:mm")
        Log.d("TimeSetInMillis:", formattedDate)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, myCalendar.timeInMillis, pendingIntent
            )
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, myCalendar.timeInMillis, pendingIntent)
        }

        Util.showToastMessage(this, "Alarm is set at : $formattedDate")
    }

    @Suppress("DEPRECATION")
    private fun isServiceRunning(reminderService: ReminderService): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (reminderService.javaClass.name == service.service.className) {
                Log.i("isMyServiceRunning?", true.toString() + "")
                return true
            }
        }
        Log.i("isMyServiceRunning?", false.toString() + "")
        return false
    }

}

