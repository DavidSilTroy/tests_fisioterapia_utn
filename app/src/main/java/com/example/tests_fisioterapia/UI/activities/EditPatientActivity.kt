package com.example.tests_fisioterapia.UI.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.capitalizeFirstLetter
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

class EditPatientActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()    //para la base de datos
    private var idPatient : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_patient)
        //Toast.makeText(applicationContext, patients.size.toString(), Toast.LENGTH_LONG).show()
        getDataDB()
    }

    fun getDataDB(){
        idPatient =  intent.getStringExtra("patientId")
        db.collection("pacientes").document(idPatient)
                .get()
                .addOnCompleteListener{
                    val name = it.result?.data!!.get("name").toString()
                    val lastname = it.result?.data!!.get("last_name").toString()
                    val age = it.result?.data!!.get("age").toString()
                    val gender = it.result?.data!!.get("gender").toString()
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
                    findViewById<TextView>(R.id.et_edit_diagnosis).text =diagnosis
                    findViewById<TextView>(R.id.et_edit_important_comments).text =importantComments
                }
                .addOnCanceledListener {
                    Toast.makeText(applicationContext,
                            "algo salió mal"
                            , Toast.LENGTH_LONG).show()
                }
    }
    fun editDataDB(){

        val name = findViewById<TextView>(R.id.et_edit_name).text.toString()
        val lastname = findViewById<TextView>(R.id.et_edit_last_name).text.toString()
        val age = findViewById<TextView>(R.id.et_edit_age).text.toString()
        val gender = findViewById<TextView>(R.id.et_edit_gender).text.toString()
        val weight = findViewById<TextView>(R.id.et_edit_weight).text.toString()
        val height =findViewById<TextView>(R.id.et_edit_height).text.toString()
        val email =findViewById<TextView>(R.id.et_edit_email).text.toString()
        val diagnosis =findViewById<TextView>(R.id.et_edit_diagnosis).text.toString()
        val importantComments = findViewById<TextView>(R.id.et_edit_important_comments).text.toString()


        val date = System.currentTimeMillis() //tiempo actual en millisegundos
        val dateString: String = SimpleDateFormat("dd/MM/yyyy, h:mm a").format(date)
        val datalist= hashMapOf<String,Any>(
                "name" to name.capitalizeFirstLetter(),
                "last_name" to lastname.capitalizeFirstLetter(),
                "age" to age,
                "gender" to gender,
                "weight" to weight,
                "height" to height,
                "email" to email,
                "edited" to dateString,
                "diagnosis" to diagnosis,
                "important_comments" to importantComments
        )

        db.collection("pacientes").document(idPatient).update(
                datalist
        )

    }


    fun btn_editPatientPhoto(view: View) {}
    fun btn_save_editPatient(view: View) {
        Toast.makeText(applicationContext, "Guardando" , Toast.LENGTH_LONG).show()
        view.visibility = View.INVISIBLE
        editDataDB()
        val intent = Intent(this, PatientInfoActivity::class.java).apply{
            putExtra("patientId", idPatient)
        }
        startActivity(intent)

}

}