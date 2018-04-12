package io.xplorer.util

import android.support.annotation.DrawableRes
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.xplorer.R
import java.io.File

object Files {
    fun listFiles(dir: File): Single<List<File>> = Single.defer {
        if (!dir.isDirectory) throw IllegalArgumentException("$dir is not a directory")
        val list = dir.listFiles()?.toList()
        if (list != null) Single.just(list)
        else Busybox.su.execute(
                "ls -1A ${dir.realPath.replace(Regex("\\s"), "\\ ")}"
        ).map { it.map { File(dir, it) } }
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    @DrawableRes
    fun getIconResId(file: File): Int {
        val mime = file.mimeType
        val type = mime.split("/").firstOrNull()
        return when {
            mime == MIME_TYPE_DIRECTORY -> R.drawable.ic_folder
            mime == "application/vnd.android.package-archive" -> R.drawable.ic_file_apk
            mime == "application/zip" || mime == "application/rar" -> R.drawable.ic_file_archive
            type == "audio" -> R.drawable.ic_file_audio
            type == "image" -> R.drawable.ic_file_image
            type == "text" -> R.drawable.ic_file_text
            type == "video" -> R.drawable.ic_file_video
            else -> R.drawable.ic_file_binary
        }
    }
}