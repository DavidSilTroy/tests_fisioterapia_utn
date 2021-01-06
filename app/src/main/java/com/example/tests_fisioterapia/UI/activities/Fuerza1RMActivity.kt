package com.example.tests_fisioterapia.UI.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

class Fuerza1RMActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fuerza_1rm1)

        val ejeFue = arrayOf("1:Triceps polea alta","2:Press francés 1 brazo con mancuerna","3:Press francés en banco con mancuerna","4:Extensiones codo desde tumbado","5:Curl brazos con pesas","6:Press banca","7:Laterales con mancuernas","8: 1/2 Squat","9:Press piernas plano inclinado","10:Extensor de cuadriceps","11:Press piernas tumbado","12:Flexores piernas en máquina","13:Isquiotibiales con lastres","14:Soleo en máquina", "15:Aductores", "16:Abductores", "17:Flexores brazos")
        val spinnerFue = findViewById<Spinner>(R.id.sp_select_ejercicio1RM)

        if (spinnerFue != null) {
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, ejeFue)
            spinnerFue.adapter = arrayAdapter

            spinnerFue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Toast.makeText(this@Fuerza1RMActivity, getString(R.string.selec_ejeF) + " " + ejeFue[position], Toast.LENGTH_SHORT).show()
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }
            }
        }
    }
}