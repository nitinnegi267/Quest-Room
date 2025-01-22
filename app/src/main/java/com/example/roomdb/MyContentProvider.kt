package com.example.roomdb

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import com.example.roomdb.room.User
import com.example.roomdb.room.UserDao
import com.example.roomdb.room.UserDatabase

const val PROVIDER = "com.example.roomdb"

class MyContentProvider : ContentProvider() {

    private lateinit var database: UserDatabase
    private lateinit var domainDao: UserDao

    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(PROVIDER, DOMAINS, DOMAINS_CODE)
        addURI(PROVIDER, DOMAINS_ITEM, DOMAINS_ITEM_CODE)
    }

    override fun onCreate(): Boolean {

        context?.let {
            val userDatabase =
                Room.databaseBuilder(context = it, UserDatabase::class.java, "user-database")
                    .build()
            domainDao = userDatabase.userDao()
        }

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            DOMAINS_CODE -> {
                domainDao.findAll()
            }

//            DOMAINS_ITEM_CODE -> {
//                domainDao.selectById(uri.lastPathSegment!!.toInt())
//            }

            else -> null
        }

    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            DOMAINS_CODE -> "vnd.android.cursor.dir/$PROVIDER/domains"
            DOMAINS_ITEM_CODE -> "vnd.android.cursor.item/$PROVIDER/domains"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (uriMatcher.match(uri)) {
            DOMAINS_CODE -> {
                val domain = User(
                    name = values!!.get("name") as String,
                    address = "Some Addresss",
                    city = "Some City",
                    phone = "Some Phone"
                )
                val rowId = domainDao.insert(domain)
                val finalUri = ContentUris.withAppendedId(uri, rowId)
                context!!.contentResolver.notifyChange(finalUri, null)
                finalUri
            }

            else -> null
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}