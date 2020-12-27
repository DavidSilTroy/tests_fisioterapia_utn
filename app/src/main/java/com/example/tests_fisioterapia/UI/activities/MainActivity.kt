package com.example.tests_fisioterapia.UI.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.PatientAdapter
import com.example.tests_fisioterapia.controllers.PatientsData
import java.util.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    var limit = 10
    //lateinit var rvPatients : RecyclerView
    var patients = listOf(
        PatientsData("Dante Alighieri",30,"Male","1.8","80kg","todo correcto con este tipo xd"),
        PatientsData("Vergil Alighieri",31,"Male","1.79","78kg","todo bien con este tipo xd"),
        PatientsData("Princesa Ana",22,"female","1.75","70kg","Libre es.. eso dice"),
            PatientsData("Vergil Alright",31,"Male","1.79","78kg","todo bien con este tipo xd"),
            PatientsData("Vergil Alright",31,"Male","1.79","78kg","todo bien con este tipo xd"),
            PatientsData("Vergil Alright",31,"Male","1.79","78kg","todo bien con este tipo xd"),
            PatientsData("Vergil Alright",31,"Male","1.79","78kg","todo bien con este tipo xd"),
            PatientsData("Vergil Alright",31,"Male","1.79","78kg","todo bien con este tipo xd"),
            PatientsData("Vergil Alright",31,"Male","1.79","78kg","todo bien con este tipo xd"),
            PatientsData("Vergil Alright",31,"Male","1.79","78kg","todo bien con este tipo xd"),
            PatientsData("Vergil Alright",31,"Male","1.79","78kg","todo bien con este tipo xd"),
            PatientsData("Vergil Alright",31,"Male","1.79","78kg","todo bien con este tipo xd"),
            PatientsData("Vergil Alright",31,"Male","1.79","78kg","todo bien con este tipo xd"),
            PatientsData("Vergil Alright",31,"Male","1.79","78kg","todo bien con este tipo xd"),
            PatientsData("Vergil Alright",31,"Male","1.79","78kg","todo bien con este tipo xd")
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Toast.makeText(applicationContext, patients.size.toString(), Toast.LENGTH_LONG).show()
        findViewById<TextView>(R.id.tv_user_name).text = "David SilTroy"
        initRecycler()



    }


    fun initRecycler(){

        val rvPatients = findViewById<RecyclerView>(R.id.rv_patients_show)
        val progbar = findViewById<ProgressBar>(R.id.progress_loading_show)
        val viprogbar = findViewById<View>(R.id.view_loading_show)
        val adapter = PatientAdapter(patients)

        rvPatients.layoutManager = LinearLayoutManager(this)

        progbar.visibility = View.VISIBLE
        viprogbar.visibility = View.VISIBLE

        //Toast.makeText(applicationContext, "hola", Toast.LENGTH_LONG).show()

        Handler().postDelayed({

            rvPatients.adapter = adapter
            viprogbar.visibility = View.INVISIBLE
            progbar.visibility = View.INVISIBLE

            rvPatients.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy>0){
                        //hacer algo al detectar desplazamiento
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })

        }, Random.nextInt(500, 5000).toLong())

    }

}