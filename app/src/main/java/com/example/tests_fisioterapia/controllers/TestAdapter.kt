package com.example.tests_fisioterapia.controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.UI.activities.AddTestActivity
import com.example.tests_fisioterapia.UI.activities.PatientInfoActivity
import com.example.tests_fisioterapia.UI.activities.SelectingTestActivity


class testData(
        val name:String,
        val pass:Boolean = false
)

class TestAdapter(val tests:List<testData>, val context: Context): RecyclerView.Adapter<TestAdapter.TestHolder>(){

    //cuantos elementos tenemos
    override fun getItemCount(): Int = tests.size
    //cuales son los datos que vamos a cargar
    override fun onBindViewHolder(holder: TestHolder, position: Int) {
       holder.render(tests[position])
    }
    //cual es el diseño que se va a usar para la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TestHolder(layoutInflater.inflate(R.layout.item_test_to_select , parent,false),context)
    }
    //como se enlazan los elementos visuales
    class TestHolder(val view: View, val context: Context): RecyclerView.ViewHolder(view){
        fun render(tests: testData){

            view.findViewById<TextView>(R.id.tv_test_name_item).text = tests.name
            if (tests.pass){
                view.setOnClickListener {
                    val intent = Intent(context.applicationContext , SelectingTestActivity::class.java).apply{
                        putExtra("testName",tests.name)
                    }
                    ContextCompat.startActivity(context, intent, Bundle())
                }
            }else{
                view.setOnClickListener {
                    val intent = Intent(context.applicationContext , AddTestActivity::class.java).apply{
                        putExtra("testName",tests.name)
                    }
                    ContextCompat.startActivity(context, intent, Bundle())
                }
            }

        }
    }


    /*
//como se enlazan los elementos visuales
    class PatientHolder(val view:View,val context: Context):RecyclerView.ViewHolder(view){
        fun render(patient:PatientsData){

            val txtPatientName = view.findViewById<TextView>(R.id.txt_patient_name_item)
            txtPatientName.text = patient.name
            view.findViewById<TextView>(R.id.txt_patient_age_item).text = patient.age.toString()
            view.findViewById<TextView>(R.id.txt_patient_gender_item).text =patient.gender
            view.findViewById<TextView>(R.id.txt_patient_weight_item).text =patient.weight
            view.findViewById<TextView>(R.id.txt_patient_height_item).text = patient.height
            view.findViewById<TextView>(R.id.txt_patient_diagnosis_item).text = patient.diagnosis
            view.findViewById<TextView>(R.id.txt_patient_edited_item).text = patient.edited

            txtPatientName.setOnClickListener {
                val intent = Intent(context.applicationContext , PatientInfoActivity::class.java).apply{
                    putExtra("patientId", patient.id)
                }
                startActivity(context,intent, Bundle())
                //Toast.makeText(view.context, "Al perfil! nararara", Toast.LENGTH_LONG).show()
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
                val intent = Intent(context.applicationContext , PatientInfoActivity::class.java).apply{
                    putExtra("patientId", patient.id)
                }
                startActivity(context,intent, Bundle())
                //Toast.makeText(view.context, "Viendo toda la info..", Toast.LENGTH_LONG).show()
            }
        }
    }



*/
}
