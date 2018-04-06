package io.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter

class ViewPagerFragmentAdapter<T>(fm: FragmentManager, private val presenter: Presenter<T>) : FragmentStatePagerAdapter(fm), MutableList<T> {
    private val fragmentMap = hashMapOf<T, Fragment>()
    private val itemsMap = hashMapOf<Any, T>()
    private val positionsMap = hashMapOf<Any, Int>()
    private val list = arrayListOf<T>()

    override fun getItem(position: Int): Fragment {
        val item = get(position)
        val fragment = fragmentMap.getOrPut(item) { presenter.onCreateFragment(item) }
        itemsMap[fragment] = item
        positionsMap[fragment] = position
        return fragment
    }

    override fun getCount(): Int = size

    override fun getItemPosition(`object`: Any): Int {
        val item = itemsMap[`object`] ?: return PagerAdapter.POSITION_NONE
        val oldPosition = positionsMap[`object`]
        val newPosition = indexOf(item)
        return when {
            newPosition < 0 -> PagerAdapter.POSITION_NONE
            oldPosition == newPosition -> PagerAdapter.POSITION_UNCHANGED
            else -> newPosition
        }
    }

    fun set(items: Collection<T>) {
        list.clear()
        list.addAll(items)
        notifyDataSetChanged()
    }

    fun swap(fromIndex: Int, toIndex: Int) {
        list.add(toIndex, list.removeAt(fromIndex))
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? = presenter.getPageTitle(get(position))

    override fun getPageWidth(position: Int): Float = presenter.getPageWidth(get(position))

    override val size: Int get() = list.size

    override fun contains(element: T): Boolean = list.contains(element)

    override fun containsAll(elements: Collection<T>): Boolean = list.containsAll(elements)

    override fun get(index: Int): T = list[index]

    override fun indexOf(element: T): Int = list.indexOf(element)

    override fun isEmpty(): Boolean = list.isEmpty()

    override fun iterator(): MutableIterator<T> = listIterator()

    override fun lastIndexOf(element: T): Int = list.lastIndexOf(element)

    override fun add(element: T): Boolean {
        val result = list.add(element)
        notifyDataSetChanged()
        return result
    }

    override fun add(index: Int, element: T) {
        list.add(index, element)
        notifyDataSetChanged()
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val result = list.addAll(index, elements)
        if (result) notifyDataSetChanged()
        return result
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val result = list.addAll(elements)
        if (result) notifyDataSetChanged()
        return result
    }

    override fun clear() {
        val count = size
        list.clear()
        if (count > 0) notifyDataSetChanged()
    }

    override fun listIterator(): MutableListIterator<T> = listIterator(0)

    override fun listIterator(index: Int): MutableListIterator<T> = object : MutableListIterator<T> {
        private val iterator = list.listIterator(index)
        override fun hasPrevious(): Boolean = iterator.hasPrevious()
        override fun nextIndex(): Int = iterator.nextIndex()
        override fun previous(): T = iterator.previous()
        override fun previousIndex(): Int = iterator.previousIndex()
        override fun add(element: T) {
            iterator.add(element)
            notifyDataSetChanged()
        }

        override fun hasNext(): Boolean = iterator.hasNext()
        override fun next(): T = iterator.next()
        override fun remove() {
            iterator.remove()
            notifyDataSetChanged()
        }

        override fun set(element: T) = iterator.set(element)
    }

    override fun remove(element: T): Boolean {
        val result = list.remove(element)
        if (result) notifyDataSetChanged()
        return result
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val result = list.removeAll(elements)
        if (result) notifyDataSetChanged()
        return result
    }

    override fun removeAt(index: Int): T {
        val result = list.removeAt(index)
        notifyDataSetChanged()
        return result
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val result = list.retainAll(elements)
        if (result) notifyDataSetChanged()
        return result
    }

    override fun set(index: Int, element: T): T {
        val result = list.set(index, element)
        notifyDataSetChanged()
        return result
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> = SubList(this, fromIndex, toIndex)

    interface Presenter<in T> {
        fun onCreateFragment(item: T): Fragment
        fun getPageTitle(item: T): CharSequence = ""
        fun getPageWidth(item: T): Float = 1f
    }
}