package com.example.tests_fisioterapia.UI.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.*
import com.google.firebase.firestore.FirebaseFirestore

class AddTestActivity:AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()    //para la base de datos
    var testslist = listOf<testData>()               //para guardar los datos a mostrar de cada test
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_test)
        getDataDB()
    }

    fun initRecyclerTests(){
        /**Guardamos la id de los Views para ubicar los datos**/
        val rvTests = findViewById<RecyclerView>(R.id.rv_select_test)
        val layoutLoading = findViewById<RelativeLayout>(R.id.layout_loading_tests)
        /****/
        val adapter = TestAdapter(testslist,this)
        /****/
        val layoutManager = LinearLayoutManager(this)
        /****/
        rvTests.layoutManager =layoutManager
        /****/
        MainControl().showRecyclerTests(layoutLoading, rvTests, adapter)
    }
    fun getDataDB(){
        var testName =""
        if(intent.hasExtra("testName")){
            testName = intent.getStringExtra("testName")
            db.collection("tipos_de_tests").document(testName).get()
                    .addOnCompleteListener{
                        if(it.result?.data.isNullOrEmpty()){
                            Toast.makeText(applicationContext,
                                    "No hay datos"
                                    , Toast.LENGTH_LONG).show()
                        }else{
                            val documents = it.result?.data!!
                            val end : Int= documents.size
                            val thelist = testslist.toMutableList()
                            for (i in 1..end){
                                thelist.add(
                                        testData(documents["tipo_$i"].toString(),true)
                                )
                            }
                            testslist = thelist.toList()
                            initRecyclerTests()
                        }
                    }
                    .addOnCanceledListener {
                        Toast.makeText(applicationContext,
                                "algo salió mal"
                                , Toast.LENGTH_LONG).show()
                    }

        }else{
            db.collection("tipos_de_tests").get()
                    .addOnCompleteListener{
                        if(it.result?.documents.isNullOrEmpty()){
                            Toast.makeText(applicationContext,
                                    "No hay datos"
                                    , Toast.LENGTH_LONG).show()
                        }else{
                            val documents = it.result?.documents
                            val end : Int= documents?.size!!-1
                            val thelist = testslist.toMutableList()
                            for (i in 0..end){
                                thelist.add(
                                        testData(documents.get(i).id,false)
                                )
                            }
                            testslist = thelist.toList()
                            initRecyclerTests()
                        }
                    }
                    .addOnCanceledListener {
                        Toast.makeText(applicationContext,
                                "algo salió mal"
                                , Toast.LENGTH_LONG).show()
                    }
        }
        //val idPatient =  intent.getStringExtra("patientId")

    }
}