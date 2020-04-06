package com.geeklabs.remindme.utils

import android.content.Context
import android.widget.Toast

object Util {

    fun showToastMessage(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}