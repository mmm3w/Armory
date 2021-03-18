package com.mitsuki.loadprogress

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun parseTest() {
        val url = "https://www.baidu.com/"
        val deco = ProgressProvider.decorateUrl(url, "asdf")
        println(deco)
        val pair = ProgressProvider.cleanUrl(deco)
        println(pair)
    }
}