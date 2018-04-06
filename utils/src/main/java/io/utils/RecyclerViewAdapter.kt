package io.utils

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RecyclerViewAdapter<T>(private val selector: PresenterSelector<T>, callback: DiffCallback<T>? = null) : RecyclerView.Adapter<RecyclerViewAdapter.PresenterViewHolder<T>>(), MutableList<T> {
    constructor(presenter: Presenter<T>, callback: DiffCallback<T>? = null) : this(object : PresenterSelector<T> {
        override fun getPresenter(item: T): Presenter<T> = presenter
    }, callback)

    private var diffCallback: DiffCallback<T>? = callback
    private val list = arrayListOf<T>()
    private val presenters = arrayListOf<Presenter<T>>()

    fun setDiffCallback(callback: DiffCallback<T>?) {
        this.diffCallback = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PresenterViewHolder<T> {
        val presenter = presenters[viewType]
        return PresenterViewHolder(presenter, parent)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: PresenterViewHolder<T>, position: Int) {
        holder.presenter.onBindView(holder.itemView, get(position))
    }

    override fun onViewRecycled(holder: PresenterViewHolder<T>) {
        super.onViewRecycled(holder)
        holder.presenter.onUnbindView(holder.itemView)
    }

    override fun getItemViewType(position: Int): Int {
        val item = get(position)
        val presenter = selector.getPresenter(item)
        var type = presenters.indexOf(presenter)
        if (type < 0) {
            type = presenters.size
            presenters.add(presenter)
        }
        return type
    }

    fun swap(fromIndex: Int, toIndex: Int) {
        list.add(toIndex, list.removeAt(fromIndex))
        notifyItemMoved(fromIndex, toIndex)
    }

    fun set(elements: Collection<T>) = diffCallback?.run { setInternal(elements, this) }
            ?: setInternal(elements)

    private fun setInternal(elements: Collection<T>) {
        list.clear()
        list.addAll(elements)
        notifyDataSetChanged()
    }

    private fun setInternal(elements: Collection<T>, callback: DiffCallback<T>) {
        val result = DiffUtil.calculateDiff(DiffCallbackWrapper(callback, list, elements))
        list.clear()
        list.addAll(elements)
        result.dispatchUpdatesTo(this)
    }

    override val size: Int get() = list.size

    override fun contains(element: T): Boolean = list.contains(element)

    override fun containsAll(elements: Collection<T>): Boolean = list.containsAll(elements)

    override fun get(index: Int): T = list[index]

    override fun indexOf(element: T): Int = list.indexOf(element)

    override fun isEmpty(): Boolean = list.isEmpty()

    override fun iterator(): MutableIterator<T> = listIterator()

    override fun lastIndexOf(element: T): Int = list.lastIndexOf(element)

    override fun add(element: T): Boolean {
        add(size, element)
        return true
    }

    override fun add(index: Int, element: T) {
        list.add(index, element)
        notifyItemInserted(index)
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val result = list.addAll(index, elements)
        if (result) notifyItemRangeInserted(index, elements.size)
        return result
    }

    override fun addAll(elements: Collection<T>): Boolean = addAll(size, elements)

    override fun clear() {
        val count = size
        list.clear()
        if (count > 0) notifyItemRangeRemoved(0, count)
    }

    override fun listIterator(): MutableListIterator<T> = listIterator(0)

    override fun listIterator(index: Int): MutableListIterator<T> = object : MutableListIterator<T> {
        private var position = index
        override fun hasPrevious(): Boolean = position > 0
        override fun nextIndex(): Int = position + 1
        override fun previous(): T = get(--position)
        override fun previousIndex(): Int = position - 1
        override fun add(element: T) = add(position, element)
        override fun hasNext(): Boolean = position < size - 1
        override fun next(): T = get(position++)
        override fun remove() {
            removeAt(position)
        }

        override fun set(element: T) {
            set(position, element)
        }
    }

    override fun remove(element: T): Boolean {
        val index = indexOf(element)
        if (index < 0) return false
        removeAt(index)
        return true
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var result = false
        elements.forEach { if (remove(it)) result = true }
        return result
    }

    override fun removeAt(index: Int): T {
        val result = list.removeAt(index)
        notifyItemRemoved(index)
        return result
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        var changed = false
        for (i in size - 1 downTo 0) {
            val obj = get(i)
            if (!elements.contains(obj)) {
                removeAt(i)
                changed = true
            }
        }
        return changed
    }

    override fun set(index: Int, element: T): T {
        val result = list.set(index, element)
        notifyItemChanged(index)
        return result
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> = SubList(this, fromIndex, toIndex)

    interface Presenter<in T> {
        fun onCreateView(inflater: LayoutInflater, parent: ViewGroup): View
        fun onBindView(view: View, item: T)
        fun onUnbindView(view: View) = Unit
    }

    interface PresenterSelector<in T> {
        fun getPresenter(item: T): Presenter<T>
    }

    interface DiffCallback<T> {
        fun areItemsTheSame(oldItem: T, newItem: T): Boolean
        fun areContentsTheSame(oldItem: T, newItem: T): Boolean
    }

    class PresenterViewHolder<T>(val presenter: Presenter<T>, parent: ViewGroup) : RecyclerView.ViewHolder(
            presenter.onCreateView(LayoutInflater.from(parent.context), parent)
    )

    private class DiffCallbackWrapper<T>(val callback: DiffCallback<T>, val oldList: Collection<T>, val newList: Collection<T>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList.elementAt(oldItemPosition)
            val newItem = newList.elementAt(newItemPosition)
            return callback.areItemsTheSame(oldItem, newItem)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList.elementAt(oldItemPosition)
            val newItem = newList.elementAt(newItemPosition)
            return callback.areContentsTheSame(oldItem, newItem)
        }

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
    }
}