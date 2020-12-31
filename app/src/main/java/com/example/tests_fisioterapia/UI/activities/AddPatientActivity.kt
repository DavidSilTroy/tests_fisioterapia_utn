package com.example.tests_fisioterapia.UI.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R

class AddPatientActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient)
    }

    fun btn_addPatient(view: View) {
        val name = findViewById<EditText>(R.id.et_add_name).text
        val age = findViewById<EditText>(R.id.et_add_age).text
        val gender = findViewById<EditText>(R.id.et_add_gender).text
        val weight = findViewById<EditText>(R.id.et_add_weight).text
        val height = findViewById<EditText>(R.id.et_add_height).text
        //val message = "New,$name,$age,$gender,$weight,$height"
        val newPatient = arrayOf(name,age,gender,weight,height)
        Toast.makeText(applicationContext, "Agregando a ${newPatient[0]}" , Toast.LENGTH_LONG).show()
        //TODO: insertar datos en la base de datos
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java).apply{
                putExtra("name_to",name)
            }
            startActivity(intent)
        }, 2000)

    }

    fun btn_addPatientPhoto(view: View) {
        Toast.makeText(applicationContext, "Esta opción estará lista en una próxima versión" , Toast.LENGTH_LONG).show()
    }

}