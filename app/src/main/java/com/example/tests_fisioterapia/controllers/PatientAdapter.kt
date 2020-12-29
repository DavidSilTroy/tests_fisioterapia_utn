package com.example.tests_fisioterapia.controllers

import android.text.Layout
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.R



class PatientsData(
        val name:String,
        val age:Int,
        val gender:String,
        val height:String,
        val weight:String,
        val diagnosis:String,
        val edited:String
)

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
            view.findViewById<TextView>(R.id.txt_patient_age_item).text = patient.age.toString()
            view.findViewById<TextView>(R.id.txt_patient_gender_item).text =patient.gender
            view.findViewById<TextView>(R.id.txt_patient_weight_item).text =patient.weight
            view.findViewById<TextView>(R.id.txt_patient_height_item).text = patient.height
            view.findViewById<TextView>(R.id.txt_patient_diagnosis_item).text = patient.diagnosis

            txtPatientName.setOnClickListener {
                Toast.makeText(view.context, "Al perfil! nararara", Toast.LENGTH_LONG).show()
            }
            view.findViewById<ImageView>(R.id.iv_arrow_down).setOnClickListener{
                ShowDetailsPatients().ShowAndHide(view)
            }
            view.findViewById<ImageView>(R.id.iv_arrow_up).setOnClickListener{
                ShowDetailsPatients().ShowAndHide(view)
            }
            view.findViewById<ImageButton>(R.id.btn_agregar_item).setOnClickListener{
                Toast.makeText(view.context, "Agregando test..", Toast.LENGTH_LONG).show()
            }
            view.findViewById<ImageButton>(R.id.btn_ver_todo_item).setOnClickListener{
                Toast.makeText(view.context, "Viendo todo..", Toast.LENGTH_LONG).show()
            }
        }
    }

}

class ShowDetailsPatients(){
    fun ShowAndHide(view:View){
        val LLdetails = view.findViewById<LinearLayout>(R.id.layout_patient_details)
        val imageArrow_up = view.findViewById<ImageView>(R.id.iv_arrow_up)
        val imageArrow_down = view.findViewById<ImageView>(R.id.iv_arrow_down)
        if (LLdetails.visibility.equals(View.VISIBLE)){
            imageArrow_down.visibility = View.VISIBLE
            imageArrow_up.visibility = View.GONE
            LLdetails.visibility = View.GONE
        }else{
            LLdetails.visibility = View.VISIBLE
            imageArrow_down.visibility = View.GONE
            imageArrow_up.visibility = View.VISIBLE
        }
    }
}
