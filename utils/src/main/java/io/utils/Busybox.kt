package io.utils

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.SingleSubject
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.charset.Charset

object Busybox {
    fun execute(su: Boolean, command: String, vararg commands: String): Single<List<String>> = Single.defer {
        val subject = SingleSubject.create<List<String>>()
        val file = File(baseContext.filesDir, "busybox")
        if (!file.exists()) FileOutputStream(file).use {
            IOUtils.copy(baseContext.assets.open(file.name), it)
        }
        if (!file.canExecute() && !file.setExecutable(true))
            subject.onError(IOException("Can't init $file"))
        val path = file.absolutePath.replace(Regex("\\s"), "\\ ")
        val process = Runtime.getRuntime().exec(if (su) "su" else "sh")
        process.outputStream.use { stream ->
            IOUtils.write("$path $command\n", stream, Charset.defaultCharset())
            commands.forEach { IOUtils.write("$path $it\n", stream, Charset.defaultCharset()) }
            IOUtils.write("exit\n", stream, Charset.defaultCharset())
        }
        process.waitFor()
        val errorText = IOUtils.toString(process.errorStream, Charset.defaultCharset()).trim()
        if (errorText.isNotEmpty()) subject.onError(IOException(errorText))
        subject.onSuccess(IOUtils.toString(process.inputStream, Charset.defaultCharset()).trim().split("\n"))
        subject
    }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}