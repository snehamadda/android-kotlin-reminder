package com.geeklabs.remindme.activites

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(com.geeklabs.remindme.R.layout.activity_splash)

        Handler().postDelayed({
            val splashIntent = Intent(this, MainActivity::class.java)
            startActivity(splashIntent)
            finish()
        }, 2000)

    }
}
