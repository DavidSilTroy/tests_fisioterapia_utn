package com.example.tests_fisioterapia.controllers

import android.text.Layout
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.R


class PatientAdapter(val patient:List<PatientsData>): RecyclerView.Adapter<PatientAdapter.PatientHolder>(){
    //cuantos elementos tenemos
    override fun getItemCount(): Int = patient.size //función en línea

    //cuales son los datos que vamos a cargar
    override fun onBindViewHolder(holder:PatientHolder, position: Int) {
        holder.render(patient[position])
    }

    //cual es el diseño que se va a usar para la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):PatientHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PatientHolder(layoutInflater.inflate(R.layout.item_patient_box , parent,false))
    }

    //como se enlazan los elementos visuales
    class PatientHolder(val view:View):RecyclerView.ViewHolder(view){
        fun render(patient:PatientsData){
            val txtPatientName = view.findViewById<TextView>(R.id.txt_patient_name_item)
            txtPatientName.text = patient.name
            view.findViewById<TextView>(R.id.txt_patient_age_item).text = "Edad: ${patient.age.toString()}"
            view.findViewById<TextView>(R.id.txt_patient_gender_item).text ="Sexo: ${patient.gender }"
            view.findViewById<TextView>(R.id.txt_patient_weight_item).text ="Peso ${patient.weight }"
            view.findViewById<TextView>(R.id.txt_patient_height_item).text = "Altura ${ patient.height }"
            view.findViewById<TextView>(R.id.txt_patient_diagnosis_item).text ="Diagnostico: ${patient.diagnosis }"

            view.setOnClickListener{
                ShowDetailsPatients().ShowAndHide(view)
            }

            view.findViewById<ImageView>(R.id.iv_arrow_down).setOnClickListener{
                ShowDetailsPatients().ShowAndHide(view)
            }
            view.findViewById<ImageView>(R.id.iv_arrow_up).setOnClickListener{
                ShowDetailsPatients().ShowAndHide(view)
            }
            view.findViewById<Button>(R.id.iv_arrow_up).setOnClickListener{}
        }
    }

}
