package com.example.notes
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONException

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, "database_name", null, 1) {
    var TableName = "Notes_Table"
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val sQuery = ("create table " + TableName
                + "(id INTEGER primary key autoincrement,title TEXT,description TEXT)")
        sqLiteDatabase.execSQL(sQuery)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i10: Int) {
        val sQuery = "drop table if exists $TableName"
        sqLiteDatabase.execSQL(sQuery)
        onCreate(sqLiteDatabase)
    }

    fun insert(title: String?, description: String?) {
        val database = writableDatabase
        val values = ContentValues()
        values.put("title", title)
        values.put("description", description)
        database.insertWithOnConflict(TableName, null, values, SQLiteDatabase.CONFLICT_REPLACE)
        database.close()
    }

    fun update(id: String, title: String, description: String) {
        val database = writableDatabase
        val sQuery = ("update " + TableName + " set title='" + title
                + "' where id='" + id + "'")
        val sQuery2 = ("update " + TableName + " set description='" + description
                + "' where id='" + id + "'")
        database.execSQL(sQuery)
        database.execSQL(sQuery2)
        database.close()
    }

    fun delete(id: String) {
        val database = writableDatabase
        val sQuery = ("delete from " + TableName + " where id='"
                + id + "'")
        database.execSQL(sQuery)
        database.close()
    }

    fun truncate() {
        val database = writableDatabase
        val sQuery1 = "delete from $TableName"
        val sQuery2 = ("delete from sqlite_sequence where name='"
                + TableName + "'")
        database.execSQL(sQuery1)
        database.execSQL(sQuery2)
        database.close()
    }
    fun getArray(): JSONArray {
            val database = readableDatabase
            val jsonArray = JSONArray()
            val sQuery = "select * from $TableName"
            val cursor = database.rawQuery(sQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val `object` = JSONObject()
                    try {
                        `object`.put("id", cursor.getString(0))
                        `object`.put("title", cursor.getString(1))
                        `object`.put("description", cursor.getString(2))
                        jsonArray.put(`object`)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
            database.close()
            return jsonArray
        }


}