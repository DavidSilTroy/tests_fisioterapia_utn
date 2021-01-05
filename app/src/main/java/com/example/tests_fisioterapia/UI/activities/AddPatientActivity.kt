package com.example.tests_fisioterapia.UI.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.capitalizeFirstLetter
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class AddPatientActivity : AppCompatActivity() {
    var user = ""
    var patientsCount = 1
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient)
        if(intent.hasExtra("userloged")){
            user = intent.getStringExtra("userloged")
        }else {
            goToMain()
        }
        db.collection("usuarios_pacientes").document(user).get()
            .addOnSuccessListener {

                while (patientsCount<901){
                    val data = it.get("paciente_$patientsCount" as String).toString()
                    if(data =="null"){
                        break
                    }
                    patientsCount++
                }
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, "Algo salió mal" , Toast.LENGTH_LONG).show()
                goToMain()
            }
    }
    fun goToMain(){
            startActivity(Intent(this, MainActivity::class.java))
    }
    fun addDataBase(newPatient:Array<String>, date : Long){
        val dateString: String = SimpleDateFormat("dd/MM/yyyy, h:mm a").format(date)
        Toast.makeText(applicationContext, "Agregando" , Toast.LENGTH_LONG).show()
        db.collection("pacientes").document(date.toString()).set(hashMapOf(
                "name" to newPatient[0],
                "last_name" to newPatient[1],
                "age" to newPatient[2],
                "gender" to newPatient[3],
                "weight" to newPatient[4],
                "height" to newPatient[5],
                "email" to newPatient[6],
                "created" to dateString,
                "diagnosis" to "...",
                "created_by" to user
        )
        ).addOnCompleteListener{
            goToMain()
        }
    }
    fun lookToDataBase(newPatient:Array<String>){
        val date = System.currentTimeMillis() //tiempo actual en millisegundos
        db.collection("usuarios_pacientes").document(user).get()
            .addOnCompleteListener {
                if(it.result?.data.toString()=="null"){
                    db.collection("usuarios_pacientes").document(user).set(hashMapOf(
                        "paciente_$patientsCount" to date)).addOnCompleteListener{
                        addDataBase(newPatient,date)
                    }
                }
                else{
                    db.collection("usuarios_pacientes").document(user).update(
                        "paciente_$patientsCount", date).addOnCompleteListener{
                        addDataBase(newPatient,date)
                }
            }
        }
    }
    fun btn_addPatient(view: View){
        view.visibility = View.INVISIBLE
        val name =      findViewById<EditText>(R.id.et_add_name)?.text.toString()
        val lastname =  findViewById<EditText>(R.id.et_add_last_name)?.text.toString()
        val age =       findViewById<EditText>(R.id.et_add_age)?.text.toString()
        val gender =    findViewById<EditText>(R.id.et_add_gender)?.text.toString()
        val weight =    findViewById<EditText>(R.id.et_add_weight)?.text.toString()
        val height =    findViewById<EditText>(R.id.et_add_height)?.text.toString()
        val email =    findViewById<EditText>(R.id.et_add_email)?.text.toString()
        when{
            name.isNullOrEmpty()    -> Toast.makeText(applicationContext, "Falta el nombre" ,Toast.LENGTH_LONG).show()
            lastname.isNullOrEmpty()-> Toast.makeText(applicationContext, "Falta el apellido", Toast.LENGTH_LONG).show()
            age.isNullOrEmpty()     -> Toast.makeText(applicationContext, "Falta la edad", Toast.LENGTH_LONG).show()
            gender.isNullOrEmpty()  -> Toast.makeText(applicationContext, "Falta el sexo", Toast.LENGTH_LONG).show()
            weight.isNullOrEmpty()  -> Toast.makeText(applicationContext, "Falta el peso" , Toast.LENGTH_LONG).show()
            height.isNullOrEmpty()  -> Toast.makeText(applicationContext, "Falta la altura" , Toast.LENGTH_LONG).show()

                else ->{
                val newPatient = arrayOf(
                        name.capitalizeFirstLetter(),
                        lastname.capitalizeFirstLetter(),
                        age,
                        gender.capitalizeFirstLetter(),
                        weight,
                        height,
                        email)
                //es importante el orden en el que se envia el array

                lookToDataBase(newPatient)
            }
        }
        Handler().postDelayed({
            view.visibility = View.VISIBLE
            goToMain()
        }, 3000)
    }
    fun btn_addPatientPhoto(view: View) {
        Toast.makeText(applicationContext, "Esta opción estará lista en una próxima versión" , Toast.LENGTH_LONG).show()
    }

}