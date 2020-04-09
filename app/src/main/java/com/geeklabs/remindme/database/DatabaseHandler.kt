package com.geeklabs.remindme.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.geeklabs.remindme.models.Reminder

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "RemindMeDB"
        private val TABLE_REMINDER = "Reminder"
        private val ID = "id"
        private val TITLE = "title"
        private val DESCRIPTION = "description"
        private val TIME = "time"
        private val DATE = "date"
        private val CREATED_TIME = "createdTime"
        private val MODIFIED_TIME = "modifiedTime"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_REMINDER_TABLE = ("CREATE TABLE " + TABLE_REMINDER + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TITLE + " TEXT,"
                + DESCRIPTION + " TEXT,"
                + TIME + " TEXT,"
                + DATE + " TEXT,"
                + CREATED_TIME + " TEXT,"
                + MODIFIED_TIME + " TEXT " + ")")
        db?.execSQL(CREATE_REMINDER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDER)
        onCreate(db)
    }

    fun saveReminder(reminder: Reminder): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(TITLE, reminder.title)
        contentValues.put(DESCRIPTION, reminder.description)
        contentValues.put(TIME, reminder.time)
        contentValues.put(DATE, reminder.date)
        contentValues.put(CREATED_TIME, System.currentTimeMillis())
        contentValues.put(MODIFIED_TIME, System.currentTimeMillis())
        // Inserting Row
        val success = db.insert(TABLE_REMINDER, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    fun getReminderById(id: Long): Reminder {
        val reminder = Reminder()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_REMINDER WHERE $ID = '$id'"
        val cursor = db.rawQuery(query, null)
        if (cursor.count < 1) {
            cursor.close()
            return reminder
        } else {
            cursor.moveToFirst()

            val id_ = Integer.parseInt(cursor.getString(0))
            val title = cursor.getString(1)
            val description = cursor.getString(2)
            val time = cursor.getString(3)
            val date = cursor.getString(4)
            val createdTime = cursor.getLong(5)
            val modifiedTime = cursor.getLong(6)

            reminder.id = id_
            reminder.title = title
            reminder.description = description
            reminder.date = date
            reminder.time = time
            reminder.createdTime = createdTime
            reminder.modifiedTime = modifiedTime
        }
        cursor.close()
        db.close()
        return reminder
    }

    //method to update data
    fun updateReminder(reminder: Reminder): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, reminder.id)
        contentValues.put(TITLE, reminder.title)
        contentValues.put(DESCRIPTION, reminder.description)
        contentValues.put(DATE, reminder.date)
        contentValues.put(TIME, reminder.time)
        contentValues.put(CREATED_TIME, reminder.createdTime)
        contentValues.put(MODIFIED_TIME, System.currentTimeMillis())

        // Updating Row
        val success = db.update(TABLE_REMINDER, contentValues, "$ID=" + reminder.id, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    //method to delete data
    fun deleteReminderById(id: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_REMINDER, "$ID=" + id, null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }

    fun getAll(): MutableList<Reminder> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("select * from $TABLE_REMINDER ORDER BY $MODIFIED_TIME DESC", null)
        val reminderList = mutableListOf<Reminder>()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val reminder = Reminder()
                val id_ = Integer.parseInt(cursor.getString(0))
                val title = cursor.getString(1)
                val description = cursor.getString(2)
                val time = cursor.getString(3)
                val date = cursor.getString(4)
                val createdTime = cursor.getLong(5)
                val modifiedTime = cursor.getLong(6)

                reminder.id = id_
                reminder.title = title
                reminder.description = description
                reminder.date = date
                reminder.time = time
                reminder.createdTime = createdTime
                reminder.modifiedTime = modifiedTime

                reminderList.add(reminder)
                cursor.moveToNext()
            }
        }
        db.close()
        return reminderList
    }

}