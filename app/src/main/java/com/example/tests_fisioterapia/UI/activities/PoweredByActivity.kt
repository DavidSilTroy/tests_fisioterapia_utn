package com.example.tests_fisioterapia.UI.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.PatientsData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PoweredByActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_powered_by)
    }
}