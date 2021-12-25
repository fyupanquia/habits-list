package com.example.myapplication.bd

import android.R
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.myapplication.Habit
import com.example.myapplication.bd.HabitEntry.DESCR_COL
import com.example.myapplication.bd.HabitEntry.IMAGE_COL
import com.example.myapplication.bd.HabitEntry.TABLE_NAME
import com.example.myapplication.bd.HabitEntry.TITLE_COL
import com.example.myapplication.bd.HabitEntry._ID
import java.io.ByteArrayOutputStream

class HabitDbTable(context: Context) {
    private val TAG = HabitDbTable::class.java.simpleName
    private val dbHelper = HabitTrainerDb(context)

    fun store(habit : Habit) : Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues()

        with(values) {
            put(TITLE_COL, habit.title)
            put(DESCR_COL, habit.description)
            put(IMAGE_COL, toByteArray(habit.image))
        }

        val id = db.transaction {
            insert(TABLE_NAME, null, values)
        }

        Log.d(TAG, "Stored new habit to the $habit")
        return id
    }
    fun readAllHabits():List<Habit> {
        val columns = arrayOf(_ID, TITLE_COL, DESCR_COL, IMAGE_COL)
        val order = "$_ID ASC"
        val db = dbHelper.readableDatabase

        val cursor = db.doQuery(table= TABLE_NAME,columns=columns,orderBy=order)
        return parseHabitsFrom(cursor)
    }
    private fun parseHabitsFrom(cursor : Cursor) : MutableList<Habit> {
        val habits = mutableListOf<Habit>()
        while(cursor.moveToNext()){
            val title = cursor.getString(TITLE_COL)
            val desc = cursor.getString(DESCR_COL)
            val bitmap = cursor.getBitmap(IMAGE_COL)
            habits.add(Habit(title, desc, bitmap))
        }
        cursor.close()
        return habits
    }
    private fun toByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0,stream)
        return stream.toByteArray()
    }
}
private  fun Cursor.getString(columnName: String) = getString(getColumnIndex(columnName))
private fun Cursor.getBitmap(columnName: String) : Bitmap {
    val bytes = getBlob(getColumnIndex(columnName))
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}


private fun SQLiteDatabase.doQuery(distinct: Boolean = true, table: String, columns : Array<String>,
                                   selection : String?=null,selectionArgs:Array<String>? = null, groupBy:String?=null,
                                   having:String?=null, orderBy:String?=null, limit: String? = null) : Cursor {

    return query(distinct,table,columns,selection,selectionArgs,groupBy,having,orderBy,limit);
}

// inline avoid create many objects for the callback   now the callback is a method of SQLiteDatabase
private inline fun <T> SQLiteDatabase.transaction(function: SQLiteDatabase.()-> T) : T{
    beginTransaction()
    var result = try {
        val returnValue = function()
        setTransactionSuccessful()
        returnValue
    } finally{
        endTransaction()
    }
    close()
    return result
}