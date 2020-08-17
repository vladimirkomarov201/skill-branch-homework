package ru.skillbranch.skillarticles.extensions

fun String?.indexesOf(substr: String, ignoreCase: Boolean = true): List<Int>{
    if (isNullOrBlank() || substr.isBlank()) return emptyList()
    return this.indexesOf(substr, ignoreCase, 0, mutableListOf())
}

private fun String?.indexesOf(substr: String, ignoreCase: Boolean = true, startIndex: Int, list: MutableList<Int>): List<Int>{
    this ?: return emptyList()
    val index = this.indexOf(substr, startIndex, ignoreCase)
    return if (index == -1)
        list
    else
        this.indexesOf(substr, ignoreCase, index + substr.length, list.apply { add(index) } )

}