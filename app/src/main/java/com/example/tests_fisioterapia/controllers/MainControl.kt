package com.example.tests_fisioterapia.controllers

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.UI.activities.MainActivity
import com.example.tests_fisioterapia.UI.fragments.IntroFragment
import java.sql.Time
import kotlin.coroutines.coroutineContext
import kotlin.random.Random


//val msj = "mostrando detalles.."
//Toast.makeText(view.context, msj, Toast.LENGTH_LONG).show()
//Toast.makeText(applicationContext,dy.toString(),Toast.LENGTH_LONG).show()

class LoadingTime(){
    //TODO:cambiar el tiempo
    fun IntroTime(): Long {
        return Random.nextInt(500, 600).toLong()
    }
    fun DBtime(): Long {
        return Random.nextInt(500, 4000).toLong()
    }

}
class MainControl(){
    fun LoadingRecycler(layoutLoading:RelativeLayout,rvPatients:RecyclerView,adapter: PatientAdapter){
        layoutLoading.visibility = View.VISIBLE
        //layoutLoading.bringToFront()
    }
    fun showRecycler(layoutLoading:RelativeLayout,rvPatients:RecyclerView,adapter: PatientAdapter){
        rvPatients.adapter = adapter
        layoutLoading.visibility = View.GONE

    }
}

fun String.capitalizeFirstLetter() = this.split(" ").joinToString(" ") { it.capitalize() }.trimEnd()

fun GoToPatientView(view: View){

}