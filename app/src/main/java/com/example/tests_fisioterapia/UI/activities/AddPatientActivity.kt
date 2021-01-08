package com.example.tests_fisioterapia.UI.activities

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.capitalizeFirstLetter
import com.example.tests_fisioterapia.network.AddPatientDB

class AddPatientActivity : AppCompatActivity() {
    var user = ""
    lateinit var databaseAdd : AddPatientDB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient)
        if(intent.hasExtra("userloged")){
            user = intent.getStringExtra("userloged")
        }else {
            this.finish()
        }
        databaseAdd = AddPatientDB(user,applicationContext)
    }
    override fun onResume() {

        if (user.isBlank()){
            databaseAdd.goToMain()
            this.finishAfterTransition()
        }else{
            databaseAdd.getNumberToPatient()
        }
        super.onResume()
    }
    private fun getETdata(){
        val name =      findViewById<EditText>(R.id.et_add_name)?.text.toString()
        val lastname =  findViewById<EditText>(R.id.et_add_last_name)?.text.toString()
        val age =       findViewById<EditText>(R.id.et_add_age)?.text.toString()
        val gender =    findViewById<EditText>(R.id.et_add_gender)?.text.toString()
        val weight =    findViewById<EditText>(R.id.et_add_weight)?.text.toString()
        val height =    findViewById<EditText>(R.id.et_add_height)?.text.toString()
        val email =    findViewById<EditText>(R.id.et_add_email)?.text.toString()
        var msj = "Agregando Paciente..."
        when{
            name.isEmpty()     || name     == "null"    ->msj= "Falta el nombre"
            lastname.isEmpty() || lastname == "null"    ->msj= "Falta el apellido"
            age.isEmpty()      || age      == "null"    ->msj= "Falta la edad"
            gender.isEmpty()   || gender   == "null"    ->msj= "Falta el sexo"
            weight.isEmpty()   || weight   == "null"    ->msj= "Falta el peso"
            height.isEmpty()   || height   == "null"    ->msj= "Falta la altura"
            age.length>3                                ->msj= "Revisa la edad"
            else                                        ->{
                val newPatient = arrayOf(
                        name.capitalizeFirstLetter(),
                        lastname.capitalizeFirstLetter(),
                        age,
                        gender.capitalizeFirstLetter(),
                        weight,
                        height,
                        email)
                //es importante el orden en el que se envia el array
                databaseAdd.addPatient(newPatient)
            }
        }
        Toast.makeText(applicationContext, msj , Toast.LENGTH_LONG).show()
    }
    fun btn_addPatient(view: View){
        view.visibility = View.INVISIBLE
        findViewById<ProgressBar>(R.id.pb_adding_patient).visibility = View.VISIBLE
        getETdata()
        Handler().postDelayed({
            view.visibility = View.VISIBLE
            findViewById<ProgressBar>(R.id.pb_adding_patient).visibility = View.GONE
        }, 3000)
    }
    fun btn_addPatientPhoto(view: View) {
        Toast.makeText(applicationContext, "Esta opción estará lista en una próxima versión" , Toast.LENGTH_LONG).show()
    }

}