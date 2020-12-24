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


class IndexActivity : AppCompatActivity() {
    var isFragmentIntroLoaded = false


    val manager = supportFragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)
        // Esto es un comentario

        ShowFragmentLoading()
    }
    fun ShowFragmentOne(){
        val transaction = manager.beginTransaction()
        val fragment = IntroFragment()
        transaction.replace(R.id.fragment_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isFragmentIntroLoaded = true
    }
    fun ShowFragmentTwo(){
        val transaction = manager.beginTransaction()
        val fragment = LoginFragment()
        transaction.replace(R.id.fragment_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isFragmentIntroLoaded = false
    }
    fun ShowFragmentLoading(){
        val transaction = manager.beginTransaction()
        val fragment = LoadingFragment()
        transaction.replace(R.id.fragment_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        Handler().postDelayed({
            //Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
            if (isFragmentIntroLoaded)
                ShowFragmentTwo()
            else
                ShowFragmentOne()
        }, 2000)
    }
    fun changeAct(view: View) {

        //val message = "Carga" //editText.text.toString()
        //val intent = Intent(this, LoadingActivity::class.java).apply{
            //putExtra(EXTRA_MESSAGE, message)
            //}
        //startActivity(intent)

        ShowFragmentLoading()
    }
}