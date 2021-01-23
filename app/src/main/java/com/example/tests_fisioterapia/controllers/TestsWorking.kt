package com.example.tests_fisioterapia.controllers

import androidx.fragment.app.Fragment
import com.example.tests_fisioterapia.UI.fragments.BurpeeFragment
import com.example.tests_fisioterapia.UI.fragments.ErrorTestFragment
import com.example.tests_fisioterapia.UI.fragments.RuffierDicksonFragment

const val TEST_1 = "Ruffier Dickson"
const val TEST_2 = "Burpee"


class TestsWorking {
    val testsOnWork = listOf(
            TEST_1,
            TEST_2
    )
    fun testFragment(testName:String):Fragment{
        when(testName){
            TEST_1 -> return RuffierDicksonFragment()
            TEST_2 -> return BurpeeFragment()

            else -> return ErrorTestFragment()
        }
    }
}