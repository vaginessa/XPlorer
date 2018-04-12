package io.xplorer.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kennyc.view.MultiStateView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.cast
import io.reactivex.rxkotlin.plusAssign
import io.xplorer.R
import io.xplorer.presenter.FileItemPresenter
import io.xplorer.util.RecyclerViewAdapter
import io.xplorer.util.bind
import io.xplorer.viewmodel.DirectoryViewModel
import io.xplorer.viewmodel.StateViewModel
import kotlinx.android.synthetic.main.fragment_directory.view.*
import kotlinx.android.synthetic.main.view_state_error.view.*
import java.io.File

class DirectoryFragment : ViewModelFragment<DirectoryViewModel>(DirectoryViewModel::class) {
    private val adapter = RecyclerViewAdapter(FileItemPresenter, FilesDiffCallback)
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        disposables += model.state
                .filter { it is StateViewModel<*>.ContentState }
                .cast<StateViewModel<List<File>>.ContentState>()
                .subscribe { adapter.set(it.content) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_directory, container, false)
        view.toolbar.bind(model.directory) {
            title = it.name
            subtitle = it.parent
        }
        view.filesList.adapter = adapter
        view.filesList.layoutManager = LinearLayoutManager(context)
        view.filesList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        view.stateView.getView(MultiStateView.VIEW_STATE_ERROR)?.retry?.setOnClickListener { model.reload() }
        view.stateView.bind(model.state) {
            viewState = when (it) {
                is StateViewModel<*>.EmptyState -> MultiStateView.VIEW_STATE_EMPTY
                is StateViewModel<*>.ErrorState -> MultiStateView.VIEW_STATE_ERROR
                is StateViewModel<*>.LoadingState -> MultiStateView.VIEW_STATE_LOADING
                else -> MultiStateView.VIEW_STATE_CONTENT
            }
            val error = (it as? StateViewModel<*>.ErrorState)?.error
            getView(MultiStateView.VIEW_STATE_ERROR)?.errorText?.text = error?.message ?: error?.toString()
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.clear()
    }

    fun back(): Boolean = model.back()

    fun open(dir: File) = model.setDirectory(dir)

    object FilesDiffCallback : RecyclerViewAdapter.DiffCallback<File> {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean = oldItem.absolutePath == newItem.absolutePath &&
                oldItem.exists() == newItem.exists() &&
                oldItem.length() == newItem.length() &&
                oldItem.canRead() == newItem.canRead() &&
                oldItem.canWrite() == newItem.canWrite() &&
                oldItem.canExecute() == newItem.canExecute()
    }
}