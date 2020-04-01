package com.geeklabs.remindme.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.geeklabs.remindme.R
import kotlinx.android.synthetic.main.activity_add_reminder.*

class AddReminderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_reminder)

        selectButton.setOnClickListener {
            Date
        }
    }
}
