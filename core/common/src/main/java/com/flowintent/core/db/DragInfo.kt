package com.flowintent.core.db

data class DragInfo(
    val index: Int,
    val offsetY: Float,
    val touchOffset: Float = 0f
)

fun calculateNewIndex(dragInfo: DragInfo, size: Int, itemHeight: Float): Int {
    val offsetItems = (dragInfo.offsetY / itemHeight).toInt()
    return (dragInfo.index + offsetItems).coerceIn(0, size - 1)
}

fun <T> MutableList<T>.swap(from: Int, to: Int) {
    if (from == to) {
        return
    }
    val item = removeAt(from)
    add(to, item)
}
