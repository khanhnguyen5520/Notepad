package com.example.notepad.DAO

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.notepad.model.Note

class NotesDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "notepad.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allnotes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_CREDATE = "credate"
        private const val COLUMN_EDITDATE = "editdate"
        private const val COLUMN_COLOR = "color"
        private const val COLUMN_SIZE = "size"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createQuery =
            "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT, $COLUMN_CREDATE TEXT, $COLUMN_EDITDATE TEXT, $COLUMN_COLOR TEXT, $COLUMN_SIZE INTEGER)"
        db?.execSQL(createQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropQuery)
        onCreate(db)
    }

    fun insertNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_CREDATE, note.creDate)
            put(COLUMN_EDITDATE, note.editDate)
            put(COLUMN_COLOR, note.color)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getAllNote(): ArrayList<Note> {
        val noteList = ArrayList<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
            val cre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREDATE))
            val edit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EDITDATE))
            val color = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR))
            val note = Note(id, title, content, cre, edit)
            note.color = color
            noteList.add(note)
        }
        cursor.close()
        db.close()
        return noteList
    }

    fun updateNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
            put(COLUMN_CREDATE, note.creDate)
            put(COLUMN_EDITDATE, note.editDate)
            put(COLUMN_COLOR, note.color)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
    }

    fun getNoteByID(noteId: Int): Note {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
        val cre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREDATE))
        val edit = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EDITDATE))
        val color = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COLOR))
        cursor.close()
        db.close()
        val note = Note(id, title, content, cre, edit)
        note.color = color
        return note
    }

    fun deleteNoteByID(noteId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
    }
}