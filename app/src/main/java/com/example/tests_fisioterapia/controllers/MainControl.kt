package com.example.tests_fisioterapia.controllers

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.UI.fragments.IntroFragment

class PatientsData(
    val name:String,
    val age:Int,
    val gender:String,
    val height:String,
    val weight:String,
    val diagnosis:String
)
class ShowDetailsPatients(){
    fun ShowAndHide(view:View){
        val msj = "mostrando detalles.."
        //Toast.makeText(view.context, msj, Toast.LENGTH_LONG).show()
        val Ldetails = view.findViewById<LinearLayout>(R.id.layout_patient_details)
        val imageArrow_up = view.findViewById<ImageView>(R.id.iv_arrow_up)
        val imageArrow_down = view.findViewById<ImageView>(R.id.iv_arrow_down)

        if (Ldetails.visibility.equals(View.VISIBLE)){
            imageArrow_down.visibility = View.VISIBLE
            imageArrow_up.visibility = View.GONE
            Ldetails.visibility = View.GONE
        }else{
            Ldetails.visibility = View.VISIBLE
            imageArrow_down.visibility = View.GONE
            imageArrow_up.visibility = View.VISIBLE
        }
    }

}
class MainControl(view: View){

}


fun GoToPatientView(view: View){

}