package io.xplorer.util

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import io.utils.MIME_TYPE_DIRECTORY
import io.utils.mimeType
import io.xplorer.R
import java.io.File

inline fun <reified T : ViewModel> Application.getViewModel(): T {
    return ViewModelProvider.AndroidViewModelFactory.getInstance(this).create(T::class.java)
}

val File.iconResId: Int
    get() {
        val mime = mimeType
        val type = mime.split("/").firstOrNull()
        return when {
            mime == MIME_TYPE_DIRECTORY -> R.drawable.ic_folder
            mime == "application/vnd.android.package-archive" -> R.drawable.ic_file_apk
            mime == "application/rar" || mime == "application/zip" -> R.drawable.ic_file_archive
            type == "image" -> R.drawable.ic_file_image
            type == "video" -> R.drawable.ic_file_video
            type == "audio" -> R.drawable.ic_file_audio
            type == "text" -> R.drawable.ic_file_text
            else -> R.drawable.ic_file_binary
        }
    }