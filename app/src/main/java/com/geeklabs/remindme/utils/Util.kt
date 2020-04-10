package com.geeklabs.remindme.utils

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

object Util {

    fun showToastMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun getFormattedDate(timeInMillis: Long, format: String): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(timeInMillis)
    }

}