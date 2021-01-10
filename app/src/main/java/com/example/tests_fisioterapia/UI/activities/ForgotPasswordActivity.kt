package com.example.tests_fisioterapia.UI.activities

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.network.AddPatientDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btn_recover:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        auth = Firebase.auth
    }

    fun sendEmail(){
        val email = getEmail()
        if(!email.isBlank()){
            auth.sendPasswordResetEmail(email).addOnCompleteListener {task->
                if(task.isSuccessful){
                    Toast.makeText(
                            applicationContext,
                            "Enviando Email para recuperar la contraseña",
                            Toast.LENGTH_LONG).show()
                    btn_recover.isEnabled = true
                }
            }.addOnFailureListener {
                var msj = "algo salió mal..intentalo después"
                val exc = "is no user record corresponding to this identifier"
                if(it.message.toString().contains(exc)){
                    msj = "Este usuario no existe"
                }
                Toast.makeText(
                        applicationContext,
                        msj,
                        Toast.LENGTH_LONG).show()
                btn_recover.isEnabled = true

            }
        }
        else{
            btn_recover.isEnabled = true
        }
    }
    fun getEmail():String{
        val email = findViewById<EditText>(R.id.et_email_forgot_password).text
        var msj = ""
        when{
            email.isNullOrEmpty()->msj="Necesita escribir un email"
            email.contains("invitado@utn.edu.ec") -> msj = "¿Y cómo se supone que accedas a ese email? usa el tuyo"
            email.contains("@utn.edu.ec") -> return email.toString()
            else->{ msj = "Ese email no es valido en esta app"}
        }
        Toast.makeText(
                applicationContext,
                msj,
                Toast.LENGTH_LONG).show()
        return ""
    }
    fun btn_recover_password(view: View) {
        view.isEnabled = false
        btn_recover = view
        sendEmail()
    }
    fun btn_powered_action_rp(view: View) {}


}