package com.example.tests_fisioterapia.UI.fragments

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.UI.activities.TestResultActivity
import com.example.tests_fisioterapia.network.GetTestData
import com.example.tests_fisioterapia.network.PATTERN_DATE
import com.example.tests_fisioterapia.network.SetTestData
import java.text.SimpleDateFormat

class ErrorTestFragment: Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_error_test,container,false)
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onDestroy() {
        this.activity!!.finish()
        super.onDestroy()
    }






}

