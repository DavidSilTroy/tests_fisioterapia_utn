package com.example.tests_fisioterapia.UI.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.UI.fragments.IntroFragment
import com.example.tests_fisioterapia.UI.fragments.LoadingFragment
import com.example.tests_fisioterapia.UI.fragments.LoginFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.random.Random


class IndexActivity : AppCompatActivity() {
    var isFragmentIntroLoaded = false


    private val manager = supportFragmentManager

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
        Toast.makeText(applicationContext, currentUser.toString(), Toast.LENGTH_LONG).show()
        if (currentUser!=null)
            loginToMain()
        else
            ShowFragmentLoading() //ir a pantalla de carga y decidir qué hacer



    }

    private fun ShowFragmentIntro(){
        val transaction = manager.beginTransaction()
        val fragment = IntroFragment()
        transaction.replace(R.id.fragment_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isFragmentIntroLoaded = true
    }
    private fun ShowFragmentLogin(){
        val transaction = manager.beginTransaction()
        val fragment = LoginFragment()
        transaction.replace(R.id.fragment_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isFragmentIntroLoaded = false
    }
    private fun ShowFragmentLoading(){
        val transaction = manager.beginTransaction()
        val fragment = LoadingFragment()
        transaction.replace(R.id.fragment_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        val random = Random.nextInt(500, 2000) //para que la pantalla de carga vaya de 500 a 2000 ms de duración randomica
        Handler().postDelayed({
            //Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            if (isFragmentIntroLoaded)
                ShowFragmentLogin()
            else
                ShowFragmentIntro()
        }, random.toLong())
    }
    fun changeFrag(view: View) {
        ShowFragmentLoading()
    }
    fun changeAct(view: View){
        //Toast.makeText(applicationContext, view.toString(), Toast.LENGTH_LONG).show()
        val message = "Carga" //editText.text.toString()
        val intent = Intent(this, MainActivity::class.java).apply{
        putExtra(EXTRA_MESSAGE, message)
        }
        startActivity(intent)
    }
    fun loginToMain(){}


}