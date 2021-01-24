package com.example.tests_fisioterapia.controllers

import androidx.fragment.app.Fragment
import com.example.tests_fisioterapia.UI.fragments.BergScaleFragment
import com.example.tests_fisioterapia.UI.fragments.BurpeeFragment
import com.example.tests_fisioterapia.UI.fragments.ErrorTestFragment
import com.example.tests_fisioterapia.UI.fragments.RuffierDicksonFragment

const val TEST_1 = "Ruffier Dickson"
const val TEST_2 = "Burpee"
const val TEST_3 = "Escala de Berg"


class TestsWorking {
    val testsOnWork = listOf(
            TEST_1,
            TEST_2,
            TEST_3
    )
    fun testFragment(testName:String):Fragment{
        when(testName){
            TEST_1 -> return RuffierDicksonFragment()
            TEST_2 -> return BurpeeFragment()
            TEST_3 -> return BergScaleFragment()
            else -> return ErrorTestFragment()
        }
    }
}