package com.example.tests_fisioterapia.UI.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Button
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
    var isFragmentIntroLoaded = false
    var theFragment = "Loading"
    val manager = supportFragmentManager


    private lateinit var auth: FirebaseAuth //para la autenticación de firebase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)
        // Initialize Firebase Auth
        auth = Firebase.auth

    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //updateUI(currentUser)
        //Toast.makeText(applicationContext, currentUser.toString(), Toast.LENGTH_LONG).show()
        if (currentUser!=null){
            //loginToMain()
        }else {
            ShowFragmentLoading() //ir a pantalla de carga y decidir qué hacer
        }

    }

    //para mostrar cualquier fragmento dentro del "when"
    private fun ShowTheFragment(theFragment: String = "Loading"){
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

    fun btn_intro_action(view: View) {
        ShowFragmentLoading()
    }
    fun btn_login_action(view: View){
        val message = "Iniciando sesión" //editText.text.toString()
        val intent = Intent(this, MainActivity::class.java).apply{
            putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }
    fun btn_powered_action(){}
    fun btn_forgot_password_action(){}



}