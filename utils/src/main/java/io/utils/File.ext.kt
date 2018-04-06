package io.utils

import android.os.FileObserver
import android.util.Log
import android.webkit.MimeTypeMap
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.io.File

data class FileEvent(
        val source: File,
        val file: File?,
        val event: Int
)

fun File.newObserver(mask: Int = FileObserver.ALL_EVENTS): Observable<FileEvent> {
    val subject = PublishSubject.create<FileEvent>()
    val observer = object : FileObserver(path, mask) {
        override fun onEvent(event: Int, path: String?) {
            val file = path?.run { File(this@newObserver, this) }
            subject.onNext(FileEvent(this@newObserver, file, event))
            Log.d("Watcher", "OnEvent: $event $file")
        }
    }
    observer.startWatching()
    return subject.doOnDispose { if (!subject.hasObservers()) observer.stopWatching() }
}

const val MIME_TYPE_DIRECTORY = "vnd.android.cursor.dir/directory"

val File.mimeType: String
    get() {
        if (isDirectory) return MIME_TYPE_DIRECTORY
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase())
                ?: "application/octet-stream"
    }