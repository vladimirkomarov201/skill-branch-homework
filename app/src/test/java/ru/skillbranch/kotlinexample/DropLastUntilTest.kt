package ru.skillbranch.kotlinexample

import org.junit.Assert
import org.junit.Test
import ru.skillbranch.kotlinexample.extensions.dropLastUntil

class DropLastUntilTest {

    @Test
    fun drop_all(){
        val list = listOf(1, 2, 3, 4, 5, 6)
        Assert.assertTrue(list.dropLastUntil { it == 10 }.isEmpty())
    }

    @Test
    fun drop_half_list(){
        val list = listOf(1, 2, 3, 4, 5, 6, 7, 8)
        Assert.assertTrue(list.dropLastUntil { it == 5}.size == 4)
    }

}