package io.utils

import android.webkit.MimeTypeMap
import java.io.File

const val MIME_TYPE_DIRECTORY = "vnd.android.cursor.dir/directory"

val File.mimeType: String
    get() {
        if (isDirectory) return MIME_TYPE_DIRECTORY
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase())
                ?: "application/octet-stream"
    }