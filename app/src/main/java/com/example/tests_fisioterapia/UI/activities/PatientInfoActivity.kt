package com.example.tests_fisioterapia.UI.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.PatientsData
import com.example.tests_fisioterapia.controllers.ShowDetailsPatients
import com.example.tests_fisioterapia.network.M_C_P
import com.google.firebase.firestore.FirebaseFirestore

class PatientInfoActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()    //para la base de datos
    private var idPatient : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_info)
        //Toast.makeText(applicationContext, patients.size.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        getDataDB()

        findViewById<TextView>(R.id.tv_diagnosis_title_info_patient).setOnClickListener{
            //Toast.makeText(applicationContext, "titulo de diagnostico", Toast.LENGTH_LONG).show()
        }
        findViewById<TextView>(R.id.tv_diagnosis_info_patient).setOnClickListener{
            //Toast.makeText(applicationContext, "el diagnostico", Toast.LENGTH_LONG).show()
        }
        findViewById<TextView>(R.id.tv_important_commets_info).setOnClickListener{
            //Toast.makeText(applicationContext, "los comentarios", Toast.LENGTH_LONG).show()
        }
        super.onResume()
    }

    /**Función para obtener los datos de la base de datos de los pacientes**/
    fun getDataDB() {
        if(intent.hasExtra("patientId")){
            idPatient = intent.getStringExtra("patientId")
            db.collection(M_C_P).document(idPatient)
                    .get()
                    .addOnCompleteListener{
                        if (it.result?.data.isNullOrEmpty()){
                            this.onBackPressed()
                        }else{
                            val name = "${it.result?.data!!.get("name")} ${it.result?.data!!.get("last_name")}"
                            val age = it.result?.data!!.get("age").toString()
                            val weight = it.result?.data!!.get("weight").toString()
                            val diagnosis = it.result?.data!!.get("diagnosis").toString()
                            var importantComments = it.result?.data!!.get("important_comments").toString()
                            var gender = it.result?.data!!.get("gender").toString()
                            if(importantComments == "null" || importantComments.isBlank()){
                                importantComments = "Agrega un comentario importante sobre este paciente"
                            }
                            if(gender.length>3){
                                gender = gender.substring(0,4)
                            }

                            findViewById<TextView>(R.id.tv_name_patient_info).text =name
                            findViewById<TextView>(R.id.tv_age_info_patient).text ="$age años"
                            findViewById<TextView>(R.id.tv_gender_info_patient).text =gender
                            findViewById<TextView>(R.id.tv_weight_info_patient).text ="$weight kg"
                            findViewById<TextView>(R.id.tv_diagnosis_info_patient).text =diagnosis
                            findViewById<TextView>(R.id.tv_important_commets_info).text =importantComments
                        }

                    }
                    .addOnCanceledListener {
                        Toast.makeText(applicationContext,
                                "algo salió mal"
                                , Toast.LENGTH_LONG).show()
                    }
        }else{
            this.onBackPressed()
        }

    }

    fun btn_powered_action(view: View) {
        //TODO:Mostrar a los creadores de la app
        Toast.makeText(applicationContext, "Estamos trabajando en ello..", Toast.LENGTH_LONG).show()
    }
    fun btn_addNewTest(view: View) {
        Toast.makeText(applicationContext, "Agregar un nuevo test", Toast.LENGTH_LONG).show()
        view.visibility = View.INVISIBLE
        val intent = Intent(this, AddTestActivity::class.java).apply{
            putExtra("patientId", idPatient)
        }
        startActivity(intent)
        view.visibility = View.VISIBLE
    }
    fun btn_editPatient(view: View) {
        view.visibility = View.INVISIBLE
        val intent = Intent(this, EditPatientActivity::class.java).apply{
            putExtra("patientId", idPatient)
        }
        startActivity(intent)
        view.visibility = View.VISIBLE
        this.finishAfterTransition()

    }

}