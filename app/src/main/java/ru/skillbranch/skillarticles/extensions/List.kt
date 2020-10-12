package ru.skillbranch.skillarticles.extensions

fun List<Pair<Int, Int>>.groupByBounds(bounds: List<Pair<Int, Int>>): List<List<Pair<Int, Int>>>{

    val result = Array<MutableList<Pair<Int, Int>>>(bounds.size) { mutableListOf() }

    forEach {
        val bound = bounds.firstOrNull { b -> it.first >= b.first && it.second <= b.second }
        if (bound != null){
            val position = bounds.indexOf(bound)
            result[position].add(it)
        }
    }

    return result.toList()
}

fun main() {
    val searchResult = listOf(Pair(2,5), Pair(8,20), Pair(22,30), Pair(45,50), Pair(70,100))
    val bounds = listOf(Pair(0,10), Pair(10,30), Pair(30,50), Pair(50,60), Pair(60,100))
    val result = listOf(listOf(Pair(2, 5), Pair(8, 10)), listOf(Pair(10, 20), Pair(22, 30)), listOf(Pair(45, 50)), emptyList(), listOf(Pair(70, 100)))
    print(searchResult.groupByBounds(bounds) == result)
}