package com.geeklabs.remindme.models

import java.io.Serializable

data class Reminder(
    var id: Long = 0,
    var title: String = " ",
    var description: String = " ",
    var time: String = " ",
    var date: String = " ",
    var createdTime: Long = 0,
    var modifiedTime: Long = 0
) : Serializable
