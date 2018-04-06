package io.xplorer.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.utils.RecyclerViewAdapter
import io.xplorer.R
import io.xplorer.util.iconResId
import kotlinx.android.synthetic.main.item_file.view.*
import java.io.File

class ItemFilePresenter : RecyclerViewAdapter.Presenter<File> {
    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View {
        return inflater.inflate(R.layout.item_file, parent, false)
    }

    override fun onBindView(view: View, item: File) {
        view.icon.setImageResource(item.iconResId)
        view.name.text = item.name
    }
}