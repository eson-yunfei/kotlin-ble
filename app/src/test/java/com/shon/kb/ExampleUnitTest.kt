package com.shon.kb

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)



    }

    @Test
    fun testListToArray(){
        val list = mutableListOf("aaaa","bbbb")
        println("list =  $list")

        val array = Array(list.size){
            return@Array list[it]
        }

        println("array =  ${array[0]}")
    }
}