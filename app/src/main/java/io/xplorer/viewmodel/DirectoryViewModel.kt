package io.xplorer.viewmodel

import android.Manifest
import android.app.Application
import android.os.FileObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.utils.Busybox
import io.utils.Permissions
import io.utils.RxSharedPreferences
import io.utils.baseContext
import io.xplorer.R
import java.io.File
import java.util.concurrent.TimeUnit

class DirectoryViewModel(app: Application) : StateViewModel<List<File>>(app) {
    private val directorySubject = BehaviorSubject.create<File>()

    val showHiddenFiles: Observable<Boolean> by lazy {
        RxSharedPreferences.getSingleton().getBoolean(R.string.preference_show_hidden_files)
    }
    val reverseListOrder: Observable<Boolean> by lazy {
        RxSharedPreferences.getSingleton().getBoolean(R.string.preference_files_list_order_reverse)
    }
    val listOrder: Observable<Order> by lazy {
        RxSharedPreferences.getSingleton().getString(R.string.preference_files_list_order).map {
            try {
                Order.valueOf(it)
            } catch (_: Throwable) {
                Order.NAME
            }
        }
    }

    fun setDirectory(dir: File) {
        if (!dir.isDirectory) throw IllegalArgumentException("$dir is file")
        directorySubject.onNext(dir)
    }

    private val dirObservable: Observable<File> = directorySubject.flatMap {
        Observable.merge(
                Observable.just(0),
                RxFileObserver.create(it)
        ).map { it }
    }

    override val state: Observable<State> by lazy {
        Observable.merge(
                Observable.just(LoadingState()),
                Observables.combineLatest(
                        directorySubject.flatMap { dir -> Observable.interval(0, 1, TimeUnit.SECONDS).map { dir } },
                        showHiddenFiles,
                        reverseListOrder,
                        listOrder,
                        Permissions.request(*PERMISSIONS).toObservable()
                ) { dir, hidden, reverse, order, permissionsResult ->
                    if (permissionsResult.denied.isNotEmpty()) ErrorState(PermissionsDeniedException(permissionsResult.denied))
                    else try {
                        val list = listFiles(dir).filter { hidden || !it.isHidden }.sortedWith(order.getComparator(reverse))
                        if (list.isEmpty()) EmptyState() else ContentState(list)
                    } catch (t: Throwable) {
                        ErrorState(t)
                    }
                }
        ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    class PermissionsDeniedException(permissions: Set<String>) : RuntimeException(
            baseContext.resources.getQuantityString(R.plurals.permissions_denied, permissions.size, permissions.joinToString("\t"))
    )

    enum class Order(private val comparator: Comparator<File>) {
        NAME(compareBy({ it.isFile }, { it.name.toLowerCase() })),
        DATE(compareBy({ it.lastModified() }, { it.name.toLowerCase() })),
        SIZE(compareBy({ calculateSize(it) }, { it.name.toLowerCase() })),
        TYPE(compareBy({ it.isFile }, { it.extension.toLowerCase() }));

        val directOrder: Comparator<File> = comparator

        val reverseOrder: Comparator<File> = Comparator { f1, f2 -> -comparator.compare(f1, f2) }

        fun getComparator(reverse: Boolean): Comparator<File> = if (reverse) reverseOrder else directOrder
    }

    companion object {
        val WATCHER_MASK = FileObserver.DELETE or
                FileObserver.DELETE_SELF or
                FileObserver.CREATE or
                FileObserver.MODIFY or
                FileObserver.MOVED_FROM or
                FileObserver.MOVED_TO or
                FileObserver.MOVE_SELF

        val PERMISSIONS = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        private fun listFiles(dir: File): List<File> {
            val list = dir.listFiles()?.toList()
            if (list != null) return list
            val command = "ls -1A ${dir.absolutePath.replace(Regex("\\s"), "\\ ")}"
            return Busybox.execute(true, command).blockingGet().map { File(dir, it) }
        }

        private fun calculateSize(file: File): Long = if (file.isDirectory) {
            var size = 0L
            listFiles(file).forEach { size += calculateSize(it) }
            size
        } else file.length()
    }
}