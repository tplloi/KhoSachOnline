package loitp.com.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.core.utilities.LAppResource
import loitp.com.model.BookFavourite
import java.util.*

class SQLiteHelper() : SQLiteOpenHelper(
    LAppResource.application,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "BookDB"
        private const val TABLE_BOOKS = "books"
        private const val KEY_ID = "id" //id
        private const val KEY_POS_CHAP = "pos" //position chap in a book
        private const val KEY_POS_WV = "poswv" //position of webview in a chap
        private const val KEY_URL = "url" //url book
        private const val KEY_THUMB = "thumb" //link thumbnail book
        private const val KEY_TIT = "tit" //title book
        private const val KEY_AUT = "aut" //author book
        private const val KEY_RQ = "rq" //who requests book
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val queryCreateBookTable =
            ("CREATE TABLE books ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + " pos INTEGER, "
                    + " poswv INTEGER,"
                    + " url TEXT, " + " thumb TEXT, " + " tit TEXT, " + " aut TEXT, " + " rq TEXT )")
        sqLiteDatabase.execSQL(queryCreateBookTable)
    }

    override fun onUpgrade(
        sqLiteDatabase: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS books")
        onCreate(sqLiteDatabase)
    }

    fun addBook(bookFavourite: BookFavourite): Boolean {
        val sqLiteDatabase = this.writableDatabase
        val isExit = dbHasData(TABLE_BOOKS, KEY_URL, bookFavourite.url)
        if (isExit) {
            //do nothing
        } else {
            val values = ContentValues()
            values.put(KEY_POS_CHAP, bookFavourite.pos)
            values.put(KEY_POS_WV, bookFavourite.poswv)
            values.put(KEY_URL, bookFavourite.url)
            values.put(KEY_THUMB, bookFavourite.thumb)
            values.put(KEY_TIT, bookFavourite.tit)
            values.put(KEY_AUT, bookFavourite.aut)
            values.put(KEY_RQ, bookFavourite.rq)
            sqLiteDatabase.insert(TABLE_BOOKS, null, values)
        }
        sqLiteDatabase.close()
        return isExit
    }

    private fun dbHasData(
        searchTable: String,
        searchColumn: String,
        searchKey: String?
    ): Boolean {
        val query = "Select * from $searchTable where $searchColumn = ?"
        return readableDatabase.rawQuery(query, arrayOf(searchKey)).moveToFirst()
    }

    val allBooks: List<BookFavourite>
        get() {
            val books: MutableList<BookFavourite> = ArrayList()
            val query = "SELECT  * FROM $TABLE_BOOKS"
            val db = this.writableDatabase
            val cursor = db.rawQuery(query, null)
            var book: BookFavourite?
            if (cursor.moveToFirst()) {
                do {
                    book = BookFavourite()
                    book.id = cursor.getString(0).toInt()
                    book.pos = cursor.getString(1).toInt()
                    book.poswv = cursor.getString(2).toInt()
                    book.url = cursor.getString(3)
                    book.thumb = cursor.getString(4)
                    book.tit = cursor.getString(5)
                    book.aut = cursor.getString(6)
                    book.rq = cursor.getString(7)
                    books.add(book)
                } while (cursor.moveToNext())
            }
            return books
        }

    fun updateBook(
        idBook: Int,
        valuePositionChapInBook: Int,
        valuePositionWebView: Int
    ) {
        val sqLiteDatabase = this.writableDatabase
        sqLiteDatabase.execSQL("UPDATE $TABLE_BOOKS SET $KEY_POS_CHAP = $valuePositionChapInBook , $KEY_POS_WV = $valuePositionWebView WHERE id = $idBook")
    }

    fun deleteBook(book: BookFavourite): Boolean {
        var state = false
        try {
            val sqLiteDatabase = this.writableDatabase
            sqLiteDatabase.delete(
                TABLE_BOOKS,
                "$KEY_ID = ?",
                arrayOf(java.lang.String.valueOf(book.id))
            )
            sqLiteDatabase.close()
            state = true
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return state
    }
}
