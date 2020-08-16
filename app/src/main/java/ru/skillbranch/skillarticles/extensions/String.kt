package ru.skillbranch.skillarticles.extensions

fun String?.indexesOf(substr: String, ignoreCase: Boolean = true): List<Int>{
    this ?: return emptyList()
//    var index = 0
//    val result = mutableListOf<Int>()
//    while (true){
//        val strIndex = this.indexOf(substr, index, ignoreCase)
//        if (strIndex == -1)
//            break
//        else{
//            index = strIndex + substr.length
//            result.add(strIndex)
//        }
//    }
//    return result

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

fun main() {
    print("lorem ipsum sum".indexesOf("sum"))
}