package com.example.tests_fisioterapia.network

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import javax.security.auth.callback.Callback

class FirestoreService {
    val firebaseFirestore = FirebaseFirestore.getInstance() //conexión directa a nuestra base de datos
    val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build() //tener datos en offline
    init { //inicializador que es como un constructor de kotlin
        firebaseFirestore.firestoreSettings = settings
    }
    //fun getData(callback: Callback<List>){
    //}
}