package io.xplorer.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.xplorer.R
import io.xplorer.util.Files
import io.xplorer.util.RecyclerViewAdapter
import kotlinx.android.synthetic.main.item_file.view.*
import java.io.File

object FileItemPresenter : RecyclerViewAdapter.Presenter<File> {
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.item_file, parent, false)
    }

    override fun onBindView(view: View, item: File) {
        view.icon.setImageResource(Files.getIconResId(item))
        view.name.text = item.name
    }
}