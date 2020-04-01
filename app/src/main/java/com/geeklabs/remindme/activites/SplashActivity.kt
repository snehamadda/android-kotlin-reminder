package com.geeklabs.remindme.activites

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.geeklabs.remindme.R.layout.activity_splash)

        handler.postDelayed({
            val splashIntent = Intent(this, MainActivity::class.java)
            startActivity(splashIntent)
            finish()
        }, 2000)

    }
}
