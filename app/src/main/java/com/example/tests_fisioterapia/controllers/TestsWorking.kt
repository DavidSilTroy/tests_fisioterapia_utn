package com.example.tests_fisioterapia.controllers

import androidx.fragment.app.Fragment
import com.example.tests_fisioterapia.UI.fragments.RuffierDicksonFragment
const val TEST_1 = "Ruffier Dickson"
class TestsWorking {
    val testsOnWork = listOf(
            TEST_1
    )
    fun testFragment(testName:String):Fragment{
        when(testName){
            TEST_1 -> return RuffierDicksonFragment()

            else -> return RuffierDicksonFragment() //TODO: Crear Fragment de error
        }
    }
}

class RuffierDicksonTest{

}