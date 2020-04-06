package com.geeklabs.remindme.activites

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.geeklabs.remindme.R
import com.geeklabs.remindme.database.DatabaseHandler
import com.geeklabs.remindme.models.Reminder
import com.geeklabs.remindme.services.ReminderAlarmBroadcastReceiver
import com.geeklabs.remindme.utils.Util
import kotlinx.android.synthetic.main.activity_add_reminder.*
import java.text.SimpleDateFormat
import java.util.*

class AddReminderActivity : AppCompatActivity() {

    private lateinit var databaseHandler: DatabaseHandler
    private val myCalendar = Calendar.getInstance()
    private var date: DatePickerDialog.OnDateSetListener? = null

    private var hour: Int = 0
    private var minute: Int = 0
    private var dayOfMonth: Int = 0
    private var monthOfYear: Int = 0
    private var year: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        databaseHandler = DatabaseHandler(this)

        date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->

            this.year = year
            this.monthOfYear = monthOfYear
            this.dayOfMonth = dayOfMonth

            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDate()
        }

        updateDate()

        selectDateButton.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = myCalendar.timeInMillis
            datePickerDialog.show()

        }

        selectTimeButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog =
                TimePickerDialog(
                    this,
                    TimePickerDialog.OnTimeSetListener(function = { _, hour, minute ->
                        updateTime(hour, minute)
                    }),
                    hour,
                    minute,
                    true
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

                val saveReminder = databaseHandler.saveReminder(reminder)
                if (saveReminder != 0L) {
                    Util.showToastMessage(this, "Reminder saved successfully")

                    myCalendar.set(year, monthOfYear + 1, dayOfMonth, hour, minute, 0)
                    Log.d("AlarmTime", "Year $year")
                    Log.d("AlarmTime", "Month $monthOfYear")
                    Log.d("AlarmTime", "Day $dayOfMonth")
                    Log.d("AlarmTime", "Hour $hour")
                    Log.d("AlarmTime", "Min $minute")

                    setRemainderAlarm(myCalendar.timeInMillis)

                    finish()
                } else {
                    Util.showToastMessage(this, "Failed to save remainder")
                }


            }
        }
    }

    private fun updateDate() {
        val dateFormat = "dd/MM/yyyy" //In which you need put here
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        dateTV.text = sdf.format(myCalendar.time)
    }

    @SuppressLint("SetTextI18n")
    private fun updateTime(hour: Int, minute: Int) {
        this.hour = hour
        this.minute = minute

        timeTV.text = "$hour : $minute"
    }

    private fun setRemainderAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val reminderIntent = Intent(this, ReminderAlarmBroadcastReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 123, reminderIntent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.set(
            AlarmManager.RTC_WAKEUP, timeInMillis,
            pendingIntent
        )
        Util.showToastMessage(this, "Alarm is set")
    }

}
