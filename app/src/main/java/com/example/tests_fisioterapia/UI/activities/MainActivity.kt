package com.example.tests_fisioterapia.UI.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    var unidad = 1
    var limit = 20
    var itemsShowed = 10
    var isLoading = true
    var patients = listOf<PatientsData>()
    val timeLoading = LoadingTime().DBtime()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*Recuerden que el layout debe estar en el AndroidManifest para que pueda ser abierto, así <activity android:name=".UI.activities.MainActivity"/>*/

        //Toast.makeText(applicationContext, patients.size.toString(), Toast.LENGTH_LONG).show()
        findViewById<TextView>(R.id.tv_user_name).text = "David SilTroy"
        findViewById<RelativeLayout>(R.id.layout_loading_patients).visibility = View.VISIBLE
        initRecycler()
        Handler().postDelayed({
            findViewById<RelativeLayout>(R.id.layout_loading_patients).visibility = View.GONE
            Toast.makeText(applicationContext, "tenemos que newPatient es ${intent.hasExtra("name_to")}" , Toast.LENGTH_LONG).show()
        }, timeLoading)

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
            MainControl().showRecycler(layoutLoading, rvPatients, adapter)
            if (limit > (itemsShowed * 2)) {
                rvPatients.scrollToPosition(limit - (itemsShowed * 2) + 3)
            }
        }, timeLoading)

        rvPatients.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                val visibleItemCount = layoutManager.childCount
                val pastVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition()
                val total = adapter.itemCount
                if (visibleItemCount + pastVisibleItem == total) {
                    MainControl().LoadingRecycler(layoutLoading, rvPatients, adapter)
                }
                if (dy > 0) {
                    if (!isLoading) {
                        if (visibleItemCount > 5) itemsShowed = visibleItemCount
                        //val msj = "$visibleItemCount y $pastVisibleItem  pero el total es $total y $itemsShowed"
                        //Toast.makeText(applicationContext,msj ,Toast.LENGTH_LONG).show()
                        if (visibleItemCount + pastVisibleItem > total) {
                            isLoading = true
                            limit += itemsShowed
                            rvPatients.removeOnScrollListener(object : RecyclerView.OnScrollListener() {})
                            initRecycler()
                        }
                    }
                }

                super.onScrolled(recyclerView, dx, dy)
            }
        })

        if(limit>(itemsShowed*2)){
            rvPatients.scrollToPosition(limit - (itemsShowed * 2) + 2) }




    }
    fun createAList(){
        patients = listOf()
        val fecha = Random.nextInt(500, 600)
        val patiData = patients.toMutableList()
        for (i in 0..limit){
            patiData.add(PatientsData("Paciente ${i}", 31, "M", "1.79", "78", "Tenditis Rotuliana", "19-10-19"))
        }
        patients = patiData.toList()
    }
    fun addToList(){
        val patiData = patients.toMutableList()
        val patientArray =  intent.getStringArrayExtra("newPatient")
        val date = System.currentTimeMillis()
        val sdf = SimpleDateFormat("MMM MM dd, yyyy h:mm a")
        val dateString: String = sdf.format(date)
        patiData.add(PatientsData(patientArray[0], patientArray[1].toInt(), patientArray[2], patientArray[4], patientArray[3], "...",dateString))
        patients = patiData.toList()
    }

    //Botones de la activity
    fun btn_OpenUserMenu(view: View){
        //Toast.makeText(applicationContext, "Botonazo de menu", Toast.LENGTH_SHORT).show()
        val the_menu = findViewById<LinearLayout>(R.id.layout_user_menu)
        if (the_menu.visibility == View.GONE){
            the_menu.visibility = View.VISIBLE
            the_menu.bringToFront()
        }else {the_menu.visibility = View.GONE}
    }
    fun userOptionsTouched(view: View){
        val txt_option = view.findViewById<TextView>(view.id)
        Toast.makeText(applicationContext, "ejecutando ${txt_option.text}... ah cierto, aún no está programado xdddd", Toast.LENGTH_LONG).show()
    }
    fun btn_addNewPatient(view: View){
        //Toast.makeText(applicationContext, "agregandooooo" , Toast.LENGTH_LONG).show()
        //FirestoreService().setDocument("Holitas","Prueba","la_id_$unidad")
        //unidad++
//        val message = "Agregando Paciente" //editText.text.toString()
//        val intent = Intent(this, AddPatientActivity::class.java).apply{
//            putExtra(AlarmClock.EXTRA_MESSAGE, message)
//        }
//        startActivity(intent)
    }

}

