package com.example.tests_fisioterapia.UI.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.UI.fragments.IntroFragment
import com.example.tests_fisioterapia.UI.fragments.LoadingFragment
import com.example.tests_fisioterapia.UI.fragments.LoginFragment
import com.example.tests_fisioterapia.controllers.LoadingTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.random.Random


class IndexActivity : AppCompatActivity() {
    val TAG = "IndexActivity"
    var isFragmentIntroLoaded = false
    var theFragment = "Loading"
    val manager = supportFragmentManager
    var user : String = ""

    private lateinit var auth: FirebaseAuth //para la autenticación de firebase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)
        // Initialize Firebase Auth
        auth = Firebase.auth

    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser // Check if user is signed in (non-null) and update UI accordingly.
        if (currentUser!=null){
            val end = currentUser.email!!.indexOf("@")
            user = currentUser.email!!.substring(0,end)
            loginToMain()
        }else {
            ShowFragmentLoading() //ir a pantalla de carga
        }
    }

    /* FUNCIONES PARA LA ACTIVITY */
    private fun ShowTheFragment(theFragment: String = "Loading"){
        //Para decidir el fragment que se va a enseñar
        val transaction = manager.beginTransaction()
        when(theFragment){
            "Intro"->{
                val fragment = IntroFragment()
                transaction.replace(R.id.fragment_holder,fragment)
                isFragmentIntroLoaded = true
            }
            "Login"-> {
                val fragment = LoginFragment()
                transaction.replace(R.id.fragment_holder,fragment)
                isFragmentIntroLoaded = false
            }
            else-> {
                val fragment = LoadingFragment()
                transaction.replace(R.id.fragment_holder,fragment)
            }
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }

    //para mostrar el fragmento de carga primero y luego los otros
    private fun ShowFragmentLoading(){
        ShowTheFragment()
        Handler().postDelayed({//para que la pantalla de carga vaya de 500 a 2000 ms de duración randomica
            //Toast.makeText(applicationContext, "algo", Toast.LENGTH_LONG).show()
            if (isFragmentIntroLoaded)
                ShowTheFragment("Login")
            else
                ShowTheFragment("Intro")
        }, LoadingTime().IntroTime())
    }


    fun loginToMain(){
        val intent = Intent(this, MainActivity::class.java).apply{
            putExtra("userloged", user)
        }
        startActivity(intent)
    }
    fun registeredUser(email:String,password:String, view: View){
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) { // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        //val user = auth.currentUser
                        loginToMain()
                    }
                    else { // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        val taskMsg = "${task.exception?.message}"
                        val msg1 = "Este usuario no existe"
                        val msg2 = "Usuario encontrado, contraseña incorrecta"
                        val msg3 = "Has intentado muchas veces con una contraseña incorrecta.. espera un momento"
                        val msg4 = "Revisa tu conección a internet"
                        val msg5 = "Algo ha salido mal.. no deberías estar viendo este mensaje, comunícate con nosotros"
                        when{
                            taskMsg.contains("There is no user record")->Toast.makeText(baseContext,msg1,Toast.LENGTH_SHORT).show()
                            taskMsg.contains("The password is invalid") ->Toast.makeText(baseContext,msg2,Toast.LENGTH_SHORT).show()
                            taskMsg.contains("We have blocked all requests") ->Toast.makeText(baseContext,msg3,Toast.LENGTH_LONG).show()
                            taskMsg.contains("Unable to resolve host") ->Toast.makeText(baseContext,msg4,Toast.LENGTH_SHORT).show()
                            else -> Toast.makeText(baseContext,msg5,Toast.LENGTH_LONG).show()
                        }
                        view.isEnabled = true
                    }
                    // ...
                }
    }

    /* BOTONES */
    fun btn_intro_action(view: View) {
        view.isEnabled = false
        Handler().postDelayed({
        ShowFragmentLoading()
        }, 500)
    }
    fun btn_login_action(view: View){

        val email = findViewById<EditText>(R.id.et_login_username).text.toString()
        val password = findViewById<EditText>(R.id.et_login_password).text.toString()
        view.isEnabled = false

        if (email.isNotEmpty() and password.isNotEmpty()){//comprobando que hay datos
            if (email.contains("@")){//comprobando que tenga @
                if (email.contains("@utn.edu.ec")){//iniciando sesion con email
                    val end = email.indexOf("@")
                    user = email.substring(0,end)
                    registeredUser(email,password,view)
                }else{//diciendo que necesita ser de la UTN xd
                    Toast.makeText(baseContext,
                            "Necesitas usar tu correo institucional UTN",
                            Toast.LENGTH_LONG).show()
                    view.isEnabled = true
                }
            }else{//iniciando sesion con usuario
                if (email.length>3){
                    user = email
                    val emailfromuser = "$email@utn.edu.ec"
                    registeredUser(emailfromuser,password,view)
                }else{
                    Toast.makeText(baseContext,
                            "Eso no es un usuario..",
                            Toast.LENGTH_LONG).show()
                    view.isEnabled = true
                }
            }
        }else{
            Toast.makeText(applicationContext,
                    "Necesita llenar todos los campos para iniciar sesión",
                    Toast.LENGTH_LONG).show()
            view.isEnabled = true
        }
    }
    fun btn_powered_action(view: View) {
        //TODO:Mostrar a los creadores de la app
        Toast.makeText(applicationContext, "Estamos trabajando en ello..", Toast.LENGTH_LONG).show()
    }

    fun btn_forgot_password_action(view: View) {
        //TODO:Enviar a recuperar la contraseña
        Toast.makeText(applicationContext, "Estamos trabajando en ello..", Toast.LENGTH_LONG).show()
    }
    fun btn_new_user_action(view: View) {
        val intent = Intent(this, RegisterUserActivity::class.java)
        startActivity(intent)
    }


}