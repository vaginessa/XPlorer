package io.utils

class SubList<T>(private val source: MutableList<T>, private val fromPosition: Int, private val toPosition: Int) : AbstractMutableList<T>() {
    private val offset = fromPosition
    private var count = toPosition - fromPosition

    init {
        if (fromPosition < 0) throw IndexOutOfBoundsException("fromPosition = $fromPosition")
        if (toPosition > source.size) throw IndexOutOfBoundsException("toPosition = $toPosition")
        if (fromPosition > toPosition) throw IllegalArgumentException("fromPosition($fromPosition) > toPosition($toPosition)")
    }

    override fun set(index: Int, element: T): T {
        rangeCheck(index)
        return source.set(index + offset, element)
    }

    override fun get(index: Int): T {
        rangeCheck(index)
        return source[index + offset]
    }

    override val size: Int get() = count

    override fun add(index: Int, element: T) {
        rangeCheckForAdd(index)
        source.add(index + offset, element)
        count++
    }

    override fun removeAt(index: Int): T {
        rangeCheck(index)
        val result = source.removeAt(index + offset)
        count--
        return result
    }

    override fun removeRange(fromIndex: Int, toIndex: Int) {
        source.removeRange(fromIndex + offset, toIndex + offset)
        count -= (toIndex - fromIndex)
    }

    override fun addAll(elements: Collection<T>): Boolean = addAll(size, elements)

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        rangeCheckForAdd(index)
        val cSize = elements.size
        if (cSize == 0)
            return false
        source.addAll(offset + index, elements)
        count += cSize
        return true
    }

    override fun iterator(): MutableIterator<T> = listIterator()

    override fun listIterator(index: Int): MutableListIterator<T> {
        rangeCheckForAdd(index)
        return object : MutableListIterator<T> {
            private val i = source.listIterator(index + offset)
            override fun hasNext(): Boolean = nextIndex() < size
            override fun next(): T = if (hasNext()) i.next() else throw NoSuchElementException()
            override fun hasPrevious(): Boolean = previousIndex() >= 0
            override fun previous(): T = if (hasPrevious()) i.previous() else throw NoSuchElementException()
            override fun nextIndex(): Int = i.nextIndex() - offset
            override fun previousIndex(): Int = i.previousIndex() - offset
            override fun set(element: T) = i.set(element)
            override fun remove() {
                i.remove()
                count--
            }

            override fun add(element: T) {
                i.add(element)
                count++
            }
        }
    }

    override fun subList(fromIndex: Int, toIndex: Int): MutableList<T> = SubList(this, fromIndex, toIndex)

    private fun rangeCheck(index: Int) {
        if (index < 0 || index >= size)
            throw IndexOutOfBoundsException(outOfBoundsMsg(index))
    }

    private fun outOfBoundsMsg(index: Int): String {
        return "Index: $index, Size: $size"
    }

    private fun rangeCheckForAdd(index: Int) {
        if (index < 0 || index > size)
            throw IndexOutOfBoundsException(outOfBoundsMsg(index))
    }
}

fun MutableList<*>.removeRange(fromIndex: Int, toIndex: Int) {
    val it = listIterator(fromIndex)
    var i = 0
    val n = toIndex - fromIndex
    while (i < n) {
        it.next()
        it.remove()
        i++
    }
}