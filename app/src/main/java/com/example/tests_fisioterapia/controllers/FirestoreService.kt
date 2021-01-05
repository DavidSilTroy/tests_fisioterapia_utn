package com.example.tests_fisioterapia.controllers

import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

const val THE_COLLECTION_NAME = "Nombre de la coleccion"

class FirestoreService(val firebaseFirestore: FirebaseFirestore) {

    fun setDocument(data: Any, collectionName: String, id: String){
        firebaseFirestore.collection(collectionName).document(id).set(data)
                .addOnSuccessListener {  }
                .addOnFailureListener {  }
    }

}

interface Callback<T>{
    fun onSuccess(result: T?)
    fun onFailed(exception: Exception)
}