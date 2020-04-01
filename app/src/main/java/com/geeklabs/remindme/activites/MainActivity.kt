package com.geeklabs.remindme.activites

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geeklabs.remindme.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        btn.setOnClickListener { view ->
            /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 .setAction("Action", null).show()*/
            val reminderIntent = Intent(this, AddReminderActivity::class.java)
            startActivity(reminderIntent)


        }
    }
}
