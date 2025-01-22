package com.example.roomdb

import android.net.Uri
import android.provider.BaseColumns

object CPsContract {
    const val AUTHORITY = "com.example.roomdb"
    val AUTHORITY_URI = Uri.parse("content://$AUTHORITY")!!

    object DomainEntry : BaseColumns {
        val TABLE_NAME = "User"
        val DOMAIN_URI = Uri.withAppendedPath(AUTHORITY_URI, TABLE_NAME)
        val TITLE = "name"
    }

}