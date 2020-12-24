package com.example.tests_fisioterapia.UI.activities

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R


class IndexActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)
        // Esto es un comentario
    }
    fun sendMessage(view: View) {
        //val editText = findViewById<EditText>(R.id.editText)
        val message = "Hola mi estimade xd" //editText.text.toString()
        val intent = Intent(this, LoginActivity::class.java).apply{
            putExtra(EXTRA_MESSAGE, message)
        }
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        startActivity(intent)
    }
}