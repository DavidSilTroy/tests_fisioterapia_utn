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
import android.support.v7.app.AppCompatActivity




class PotenciaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_potencia)

        val saltosPot = arrayOf("Salto de longitud con carrera de impulso", "Salto longitudinal sin carrera de impulso", "Salto vertical", "Salto horizontal")
        val spinnerPot = findViewById<Spinner>(R.id.sp_select_salto_testPotencia)

        if (spinnerPot != null) {
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, saltosPot)
            spinnerPot.adapter = arrayAdapter

            spinnerPot.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    Toast.makeText(this@PotenciaActivity, getString(R.string.selec_salto) + " " + saltosPot[position], Toast.LENGTH_SHORT).show()
        }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Code to perform some action when nothing is selected
                }



}