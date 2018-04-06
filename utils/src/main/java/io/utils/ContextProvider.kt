package io.utils

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri

class ContextProvider : ContentProvider() {
    override fun insert(uri: Uri?, values: ContentValues?): Uri? = null
    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor? = null
    override fun onCreate(): Boolean {
        providerInstance = this
        return true
    }

    override fun update(uri: Uri?, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun getType(uri: Uri?): String? = null
}

private lateinit var providerInstance: ContentProvider

val baseContext: Context get() = providerInstance.context