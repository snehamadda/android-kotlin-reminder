package com.geeklabs.remindme.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.geeklabs.remindme.models.Reminder

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "RemindMeDB"
        private const val TABLE_REMINDER = "Reminder"
        private const val ID = "id"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val TIME = "time"
        private const val DATE = "date"
        private const val CREATED_TIME = "createdTime"
        private const val MODIFIED_TIME = "modifiedTime"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE " + TABLE_REMINDER + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TITLE + " TEXT,"
                + DESCRIPTION + " TEXT,"
                + TIME + " TEXT,"
                + DATE + " TEXT,"
                + CREATED_TIME + " TEXT,"
                + MODIFIED_TIME + " TEXT " + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_REMINDER")
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

    fun getReminderById(reminderId: Long): Reminder {
        val reminder = Reminder()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_REMINDER WHERE $ID = '$reminderId'"
        val cursor = db.rawQuery(query, null)
        if (cursor.count < 1) {
            cursor.close()
            return reminder
        } else {
            cursor.moveToFirst()

            val id = cursor.getString(0).toLong()
            val title = cursor.getString(1)
            val description = cursor.getString(2)
            val time = cursor.getString(3)
            val date = cursor.getString(4)
            val createdTime = cursor.getLong(5)
            val modifiedTime = cursor.getLong(6)

            reminder.id = id
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
    fun deleteReminderById(id: Long): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id) // EmpModelClass UserId
        // Deleting Row
        val rowId = db.delete(TABLE_REMINDER, "$ID=$id", null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return rowId
    }

    fun getAll(): MutableList<Reminder> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("select * from $TABLE_REMINDER ORDER BY $MODIFIED_TIME DESC", null)
        val reminderList = mutableListOf<Reminder>()

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val reminder = Reminder()
                val id = cursor.getString(0).toLong()
                val title = cursor.getString(1)
                val description = cursor.getString(2)
                val time = cursor.getString(3)
                val date = cursor.getString(4)
                val createdTime = cursor.getLong(5)
                val modifiedTime = cursor.getLong(6)

                reminder.id = id
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
        cursor.close()
        db.close()
        return reminderList
    }

}