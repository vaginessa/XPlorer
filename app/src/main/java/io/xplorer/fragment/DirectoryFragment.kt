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
import io.utils.RecyclerViewAdapter
import io.utils.bind
import io.xplorer.R
import io.xplorer.presenter.ItemFilePresenter
import io.xplorer.util.getViewModel
import io.xplorer.viewmodel.DirectoryViewModel
import io.xplorer.viewmodel.StateViewModel
import kotlinx.android.synthetic.main.fragment_directory.view.*
import kotlinx.android.synthetic.main.view_state_error.view.*
import java.io.File

class DirectoryFragment : PageFragment<File>() {
    private val disposable = CompositeDisposable()
    private val model by lazy { activity!!.application.getViewModel<DirectoryViewModel>() }
    private val adapter = RecyclerViewAdapter(ItemFilePresenter(), FilesDiffCallback())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.setDirectory(item)
        disposable += model.state
                .filter { it is StateViewModel<*>.ContentState }
                .cast<StateViewModel<List<File>>.ContentState>()
                .subscribe {
                    adapter.set(it.content)
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_directory, container, false)
        view.toolbar.title = item.name
        view.toolbar.subtitle = item.parent
        view.filesList.adapter = adapter
        view.filesList.layoutManager = LinearLayoutManager(context)
        view.filesList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        view.stateView.bind(model.state) {
            when (it) {
                is StateViewModel<*>.LoadingState -> viewState = MultiStateView.VIEW_STATE_LOADING
                is StateViewModel<*>.EmptyState -> viewState = MultiStateView.VIEW_STATE_EMPTY
                is StateViewModel<*>.ContentState -> viewState = MultiStateView.VIEW_STATE_CONTENT
                is StateViewModel<*>.ErrorState -> {
                    viewState = MultiStateView.VIEW_STATE_ERROR
                    getView(MultiStateView.VIEW_STATE_ERROR)?.errorText?.text = it.error.message ?: it.error.toString()
                }
            }
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
    }

    class FilesDiffCallback : RecyclerViewAdapter.DiffCallback<File> {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean = oldItem.absolutePath == newItem.absolutePath
        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean =
                oldItem.absolutePath == newItem.absolutePath &&
                        oldItem.length() == newItem.length() &&
                        oldItem.lastModified() == newItem.lastModified() &&
                        oldItem.canExecute() == newItem.canExecute() &&
                        oldItem.canRead() == newItem.canRead() &&
                        oldItem.canWrite() == newItem.canWrite()
    }
}