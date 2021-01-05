package com.example.tests_fisioterapia.UI.activities

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import com.google.firebase.firestore.FirebaseFirestore

class EditPatientActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()    //para la base de datos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_patient)
        //Toast.makeText(applicationContext, patients.size.toString(), Toast.LENGTH_LONG).show()
        getDataDB()


    }

    fun getDataDB(){
        val id = intent.getStringExtra("patientId")
        db.collection("pacientes").document(id)
                .get()
                .addOnCompleteListener{
                    val name = it.result?.data!!.get("name").toString()
                    val lastname = it.result?.data!!.get("last_name").toString()
                    val age = it.result?.data!!.get("age").toString()
                    val gender = it.result?.data!!.get("gender").toString().substring(0,4)
                    val weight = it.result?.data!!.get("weight").toString()
                    val height = it.result?.data!!.get("height").toString()
                    val email = it.result?.data!!.get("email").toString()
                    val diagnosis = it.result?.data!!.get("diagnosis").toString()
                    val importantComments = it.result?.data!!.get("important_comments").toString()

                    findViewById<TextView>(R.id.et_edit_name).text =name
                    findViewById<TextView>(R.id.et_edit_last_name).text =lastname
                    findViewById<TextView>(R.id.et_edit_age).text =age
                    findViewById<TextView>(R.id.et_edit_gender).text =gender
                    findViewById<TextView>(R.id.et_edit_weight).text =weight
                    findViewById<TextView>(R.id.et_edit_height).text =height
                    findViewById<TextView>(R.id.et_edit_email).text =email
                    //findViewById<TextView>(R.id.et_).text =diagnosis
                    //findViewById<TextView>(R.id.tv_important_commets_info).text =importantComments

                }
                .addOnCanceledListener {
                    Toast.makeText(applicationContext,
                            "algo salió mal"
                            , Toast.LENGTH_LONG).show()
                }
    }

    fun btn_editPatientPhoto(view: View) {}

}