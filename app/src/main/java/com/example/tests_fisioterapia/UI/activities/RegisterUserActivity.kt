package com.example.tests_fisioterapia.UI.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterUserActivity: AppCompatActivity() {
    val TAG = "IndexActivity"
    private lateinit var auth: FirebaseAuth //para la autenticación de firebase
    var msj = ""
    lateinit var btn_register:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)
        auth = Firebase.auth
    }
    fun getETdata(){
        val email = findViewById<EditText>(R.id.et_register_email).text.toString()
        val password = findViewById<EditText>(R.id.et_register_password).text.toString()
        if(checkEmail(email)){
            registerAUser(email,password)
        }else{
            btn_register.isEnabled = true
            Toast.makeText(applicationContext,
                    msj,
                    Toast.LENGTH_LONG).show()
        }

    }
    fun checkEmail(email :String):Boolean{
        when{
            email.contains("@utn.edu.ec")->return true
            email == "null" -> {
                msj = "Debe escribir un correo"
                return false
            }
            email.isBlank() -> {
                msj = "Debe escribir un correo"
                return false
            }
            email.contains("invitado@utn.edu.ec")-> {
                msj = "Este correo se debe escribir para iniciar sesión"
                registerToLogin()
                return false
            }
            else-> return false
        }
    }
    fun registerAUser(email:String,password:String){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        Log.d(TAG, "createUserWithEmail:success")
                        user!!.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Log.d(TAG, "Email sent.")
                                        msj = "Revisa tu email para confirmar usuario"
                                        Toast.makeText(applicationContext,
                                                msj,
                                                Toast.LENGTH_LONG).show()
                                        val intent = Intent(this, AddUserDataActivity::class.java)
                                        startActivity(intent)
                                        btn_register.isEnabled = true
                                        this.finishAfterTransition()
                                    }
                                }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        val taskMsg = "${task.exception?.message}"
                        msj = taskMsg.toString()
                        when{
                            taskMsg.contains("Password should be at least 6")-> msj = "La contraseña debe tener al menos 6 caracteres"
                            taskMsg.contains("The email address is already in use") -> msj = "Este correo ya está registrado"
                            taskMsg.contains("We have blocked all requests") -> msj = ""
                            taskMsg.contains("Unable to resolve host") -> msj = ""
                        }
                        btn_register.isEnabled = true
                        Toast.makeText(applicationContext,
                                msj,
                                Toast.LENGTH_LONG).show()
                    }
                    // ...
                }
    }

    fun registerToLogin() {
        val intent = Intent(this, IndexActivity::class.java)
        startActivity(intent)
    }

    fun btn_powered_action_r(view: View) {}
    fun btn_register_action(view: View) {
        view.isEnabled = false
        btn_register = view
        getETdata()
    }
}