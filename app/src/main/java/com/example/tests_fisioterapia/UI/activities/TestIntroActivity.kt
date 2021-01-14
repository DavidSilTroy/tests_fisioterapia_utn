package com.example.tests_fisioterapia.UI.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.testData
import com.google.firebase.firestore.FirebaseFirestore

class TestIntroActivity: AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()    //para la base de datos
    private var idP = ""
    private var testName = ""
    private var testType = ""
    lateinit var dataList : MutableMap<String,String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_intro)
        dataList = hashMapOf()
        idP =  intent.getStringExtra("patientId")
        testName =  intent.getStringExtra("testName")
        testType =  intent.getStringExtra("testType")
    }

    override fun onStart(){
        getDBdata()
        super.onStart()
    }

    fun getDBdata(){
        db.collection("tipos_de_tests")
                .document(testType)
                .collection("information")
                .document(testName)
                .get()
                .addOnCompleteListener {
                    if(it.result?.data.isNullOrEmpty()){
                        Toast.makeText(applicationContext,
                                "No hay datos"
                                , Toast.LENGTH_LONG).show()
                    }else{
                        val documents = it.result?.data!!

                        dataList["name"] = documents["name"].toString()
                        dataList["description"] = documents["description"].toString()
                        dataList["method"] = documents["method"].toString()
                        showETdata()
                    }
                }
    }

    fun showETdata(){
        val title = findViewById<TextView>(R.id.tv_title_test_intro)
        val description = findViewById<TextView>(R.id.tv_resume_test_intro)
        val method = findViewById<TextView>(R.id.tv_method_test_intro)

        title.text       = dataList["name"]
        description.text = dataList["description"]
        method.text      = dataList["method"]
    }

    fun btn_test_start(view: View) {
        Toast.makeText(applicationContext,
                "Pronto estará disponible este test!"
                , Toast.LENGTH_LONG).show()
    }
}