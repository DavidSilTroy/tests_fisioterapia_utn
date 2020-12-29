package com.example.tests_fisioterapia.UI.activities

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.service.autofill.Dataset
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.core.view.contains
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.LoadingTime
import com.example.tests_fisioterapia.controllers.MainControl
import com.example.tests_fisioterapia.controllers.PatientAdapter
import com.example.tests_fisioterapia.controllers.PatientsData
import java.security.AccessControlContext
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    var limit = 20
    var itemsShowed = 10
    var isLoading = true
    var patients = listOf<PatientsData>()
    val timeLoading = LoadingTime().DBtime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Toast.makeText(applicationContext, patients.size.toString(), Toast.LENGTH_LONG).show()
        findViewById<TextView>(R.id.tv_user_name).text = "David SilTroy"
        findViewById<RelativeLayout>(R.id.layout_loading_patients).visibility = View.VISIBLE
        initRecycler()
        Handler().postDelayed({
            findViewById<RelativeLayout>(R.id.layout_loading_patients).visibility = View.GONE
        },timeLoading)
    }
    fun initRecycler(){
        createAList()
        val rvPatients = findViewById<RecyclerView>(R.id.rv_patients_show)
        val layoutLoading = findViewById<RelativeLayout>(R.id.layout_loading_more_patients)
        val adapter = PatientAdapter(patients)
        val layoutManager = LinearLayoutManager(this)
        rvPatients.layoutManager =layoutManager

        Handler().postDelayed({
            isLoading = false
            MainControl().showRecycler(layoutLoading,rvPatients,adapter)
            if(limit>(itemsShowed*2)){
                rvPatients.scrollToPosition(limit-(itemsShowed*2)+3) }
        }, timeLoading)

        rvPatients.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = adapter.itemCount
                if (visibleItemCount+pastVisibleItem==total){
                    MainControl().LoadingRecycler(layoutLoading,rvPatients,adapter)}
                if (dy>0){
                    if(!isLoading){
                        if(visibleItemCount>5) itemsShowed = visibleItemCount
                        //val msj = "$visibleItemCount y $pastVisibleItem  pero el total es $total y $itemsShowed"
                        //Toast.makeText(applicationContext,msj ,Toast.LENGTH_LONG).show()
                        if (visibleItemCount+pastVisibleItem>total){
                            isLoading = true
                            limit+=itemsShowed
                            rvPatients.removeOnScrollListener(object: RecyclerView.OnScrollListener(){})
                            initRecycler()
                        }
                    }
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })

        if(limit>(itemsShowed*2)){
            rvPatients.scrollToPosition(limit-(itemsShowed*2)+2) }




    }
    fun createAList(){
        patients = listOf()
        val patiData = patients.toMutableList()
        for (i in 0..limit){
            patiData.add(PatientsData("Paciente ${i}",31,"M","1.79","78","Tenditis Rotuliana","19-10-19"))
        }
        patients = patiData.toList()
    }

    fun btn_OpenUserMenu(view: View){
        //Toast.makeText(applicationContext, "Botonazo de menu", Toast.LENGTH_SHORT).show()
        val the_menu = findViewById<LinearLayout>(R.id.layout_user_menu)
        if (the_menu.visibility == View.GONE){
            the_menu.visibility = View.VISIBLE
            the_menu.bringToFront()
        }else {the_menu.visibility = View.GONE}
    }
    fun userOptionsTouched(view:View){
        val txt_option = view.findViewById<TextView>(view.id)
        Toast.makeText(applicationContext, "ejecutando ${txt_option.text}... ah cierto, aún no está programado xdddd" , Toast.LENGTH_LONG).show()
    }
    fun btn_addNewPatient(view: View){
        Toast.makeText(applicationContext, "agregandooooo" , Toast.LENGTH_LONG).show()
    }

}

