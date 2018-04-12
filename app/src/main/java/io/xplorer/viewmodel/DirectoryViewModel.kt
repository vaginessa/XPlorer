package io.xplorer.viewmodel

import android.Manifest
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.xplorer.R
import io.xplorer.util.*
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class DirectoryViewModel : StateViewModel<List<File>>() {
    private val dirSubject = BehaviorSubject.create<File>()
    private val dirStack = Stack<File>()
    private val listComparator: Observable<Comparator<File>> = Observables.combineLatest(
            RxSharedPreferences.getSingleton().getBoolean(R.string.preference_files_sort_order_invert),
            RxSharedPreferences.getSingleton()
                    .getString(R.string.preference_files_sort_order)
                    .map { EnumUtil.valueOf(it, FilesListOrder.NAME) }
                    .map {
                        when (it) {
                            FilesListOrder.NAME -> compareBy<File>({ it.isFile }, { it.name.toLowerCase() })
                            FilesListOrder.DATE -> compareBy({ it.lastModified() }, { it.name.toLowerCase() })
                            FilesListOrder.SIZE -> compareBy({ it.isFile }, { it.length() }, { it.name.toLowerCase() })
                            FilesListOrder.TYPE -> compareBy({ it.isFile }, { it.mimeType }, { it.name.toLowerCase() })
                        }
                    }
    ) { invert, comparator -> comparator.invert(invert) }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

    private val showHidden = RxSharedPreferences.getSingleton().getBoolean(R.string.preference_show_hidden_files)

    val directory: Observable<File> = dirSubject.filter { it.isDirectory }

    fun setFilesOrder(order: FilesListOrder, invert: Boolean) {
        RxSharedPreferences.getSingleton().edit()
                .putString(R.string.preference_files_sort_order, order.name)
                .putBoolean(R.string.preference_files_sort_order_invert, invert)
                .apply()
    }

    fun showHidden(show: Boolean) {
        RxSharedPreferences.getSingleton().edit()
                .putBoolean(R.string.preference_show_hidden_files, show)
                .apply()
    }

    fun setDirectory(directory: File) {
        dirSubject.onNext(directory)
        dirStack.push(directory)
    }

    fun back(): Boolean {
        if (!dirStack.empty()) dirStack.pop()
        if (dirStack.empty()) return false
        dirSubject.onNext(dirStack.peek())
        return true
    }

    fun reload() {
        if (dirStack.empty()) return
        dirSubject.onNext(dirStack.peek())
    }

    override fun provideState(): Observable<State> {
        val subject = BehaviorSubject.create<State>()
        return directory.doOnNext {
            subject.onNext(LoadingState())
        }.flatMap { dir ->
            Permissions.request(*PERMISSIONS).toObservable().flatMap {
                if (it.denied.isEmpty())
                    Observable.interval(0, 1, TimeUnit.SECONDS).flatMap {
                        Observables.combineLatest(
                                Files.listFiles(dir).toObservable(),
                                listComparator,
                                showHidden
                        ) { files, order, hidden ->
                            (if (hidden) files else files.filter { !it.isHidden }).sortedWith(order)
                        }
                    }.doOnNext {
                        subject.onNext(if (it.isEmpty()) EmptyState() else ContentState(it))
                    }.doOnError {
                        subject.onNext(ErrorState(it))
                    }.flatMap { subject }
                else Observable.just(ErrorState(PermissionsDeniedException(it.denied)))
            }
        }
    }

    companion object {
        val PERMISSIONS = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}