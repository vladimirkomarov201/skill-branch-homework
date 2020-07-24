package ru.skillbranch.kotlinexample.extensions

fun <T> List<T>.dropLastUntil(predicate: (T) -> Boolean): List<T>{
    val result = this.toMutableList()
    for (index in this.lastIndex downTo 0){
        if (predicate.invoke(result[index])){
            result.removeAt(index)
            break
        } else {
            result.removeAt(index)
        }
    }
    return result
}