package com.example.tests_fisioterapia.UI.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R

class TestResultActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_result)
        val testName = intent.getStringExtra("test_name")
        val result = intent.getStringExtra("result_test")
        val extra_info = intent.getStringExtra("result_info_extra")
        findViewById<TextView>(R.id.tv_result_patient_test).text = result
        findViewById<TextView>(R.id.tv_test_result_extra_info).text = extra_info
        findViewById<TextView>(R.id.tv_title_test_result).text = testName


    }

    fun btn_test_start(view: View) {
        view.visibility = View.INVISIBLE
        val intent = Intent(this, MainActivity::class.java)
        this.finishAfterTransition()
        startActivity(intent)
    }
}