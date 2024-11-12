package com.example.danhba

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "qldb.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_CONTACTS = "contacts"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_EMAIL = "email"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_CONTACTS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_NAME TEXT,"
                + "$COLUMN_PHONE TEXT,"
                + "$COLUMN_EMAIL TEXT" + ")")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun addContact(contact: Contact) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, contact.name)
        values.put(COLUMN_PHONE, contact.phone)
        values.put(COLUMN_EMAIL, contact.email)
        db.insert(TABLE_CONTACTS, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getContactById(id: Int): Contact {
        val db = this.readableDatabase
        val cursor: Cursor = db.query(TABLE_CONTACTS, null, "$COLUMN_ID=?", arrayOf(id.toString()), null, null, null)
        cursor.moveToFirst()
        val contact = Contact(
            cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
            cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)),
            cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
        )
        cursor.close()
        return contact
    }

    fun updateContact(contact: Contact) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, contact.name)
        values.put(COLUMN_PHONE, contact.phone)
        values.put(COLUMN_EMAIL, contact.email)
        db.update(TABLE_CONTACTS, values, "$COLUMN_ID=?", arrayOf(contact.id.toString()))
        db.close()
    }

    fun deleteContact(id: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_CONTACTS, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
    }

    @SuppressLint("Range")
    fun getAllContacts(searchQuery: String = ""): List<Map<String, Any>> {
        val contacts = mutableListOf<Map<String, Any>>()
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_CONTACTS,
            null,
            "$COLUMN_NAME LIKE ?",
            arrayOf("%$searchQuery%"),
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val contact = mapOf(
                    "id" to cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    "name" to cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    "phone" to cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)),
                    "email" to cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL))
                )
                contacts.add(contact)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contacts
    }
}
