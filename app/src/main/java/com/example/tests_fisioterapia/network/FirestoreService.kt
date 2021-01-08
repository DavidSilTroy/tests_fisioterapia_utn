package com.example.tests_fisioterapia.network

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.UI.activities.MainActivity
import com.example.tests_fisioterapia.UI.activities.PatientInfoActivity
import com.example.tests_fisioterapia.controllers.capitalizeFirstLetter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import java.text.SimpleDateFormat
import javax.security.auth.callback.Callback

/**M_C_ == Main_Collection_ == Colección Principal **/
const val M_C_P = "pacientes"
const val M_C_U = "usuarios"
const val M_C_TT = "tipos_de_tests"
const val M_C_PT = "pacientes_test"
const val M_C_UP = "usuarios_pacientes"

/**S_C_ == Secondary_Collection_ == Colección Secundaria **/
const val S_C_R = "Register"

/**D_ == Document_ == Documento **/
const val D_M = "main"

const val PATTERN_DATE = "dd/MM/yyyy, h:mm a"


class FirestoreService {
    val firebaseFirestore = FirebaseFirestore.getInstance()
    val settings = FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build() //tener datos en offline
    init { //inicializador que es como un constructor de kotlin
        firebaseFirestore.firestoreSettings = settings
    }
    //fun getData(callback: Callback<List>){
    //}
}

class  AddPatientDB(val user:String, val context: Context){
    private val db = FirebaseFirestore.getInstance() //conexión directa a nuestra base de datos
    private var date :Long = 0
    var number = 1
    var newPatient:Array<String> = arrayOf()
    fun goToMain(){
        val intent = Intent(context.applicationContext , MainActivity::class.java)
        startActivity(context, intent, Bundle())
    }
    fun getNumberToPatient(){
        db.collection(M_C_UP).document(user).get()
                .addOnSuccessListener {
                    while(number<901){
                        val data = it.get("paciente_$number" as String).toString()
                        if(data=="null") break
                        number++
                    }
                    //TODO:Imprimir mensaje de que ya se tiene más de 900 pacientes
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Algo salió mal" , Toast.LENGTH_LONG).show()
                    goToMain()
                }
    }
    fun addPatient(patientArray:Array<String>){
        newPatient = patientArray
        lookToDataBase()
    }
    fun lookToDataBase(){
        date = System.currentTimeMillis() //tiempo actual en millisegundos
        db.collection("usuarios_pacientes").document(user).get()
                .addOnCompleteListener {
                    if(it.result?.data.isNullOrEmpty()){
                        db.collection(M_C_UP).document(user).set(hashMapOf(
                                "paciente_$number" to "p${date}")).addOnCompleteListener{
                            addDataBase()
                        }
                    }
                    else{
                        db.collection(M_C_UP).document(user).update(
                                "paciente_$number", "p${date}").addOnCompleteListener{
                            addDataBase()
                        }
                    }
                }
    }
    @SuppressLint("SimpleDateFormat")
    fun addDataBase(){
        //TODO: saber que no hay internet para agregar los datos a la nube
        val dateString: String = SimpleDateFormat(PATTERN_DATE).format(date)
        Toast.makeText(context, "Agregado!" , Toast.LENGTH_LONG).show()
        //Creando el documento con los valores en la base de datos
        db.collection(M_C_P).document("p$date").set(hashMapOf(
                "edited" to dateString,
                "name" to newPatient[0],
                "last_name" to newPatient[1],
                "age" to newPatient[2],
                "gender" to newPatient[3],
                "weight" to newPatient[4],
                "height" to newPatient[5],
                "email" to newPatient[6],
                "diagnosis" to "Sin Diagnostico",
                "important_comments" to "Agrega un comentario"
        )
        ).addOnCompleteListener{
            //Creando subcolección para registro de edición
            db.collection(M_C_P).document("p$date").collection(S_C_R).document(D_M)
                    .set(mapOf(
                            "creation" to dateString,
                            "created_by" to user
                    )).addOnCompleteListener {
                        goToMain()
                    }
        }
    }
}
class EditPatientDB(val idP:String , val user:String, val context: Context){
    private val db = FirebaseFirestore.getInstance() //conexión directa a nuestra base de datos
    private var date :Long = 0

    fun saveData(datalist:MutableMap<String,Any>){
        date = System.currentTimeMillis() //tiempo actual en millisegundos
        val dateString: String = SimpleDateFormat(PATTERN_DATE).format(date)
        datalist["edited"] = dateString
        db.collection(M_C_P).document(idP).update(
                datalist
        )
    }

    fun saveRegister(datalist:MutableMap<String,Any>){

        date = System.currentTimeMillis() //tiempo actual en millisegundos
        val dateString: String = SimpleDateFormat(PATTERN_DATE).format(date)

        datalist["edited"] = dateString
        datalist["edited_by"] = user

        db.collection(M_C_P).document(idP).collection(S_C_R).document("r$date").set(
                datalist
        )
    }


}

class GetPatientDB(val idP:String ){
    private val db = FirebaseFirestore.getInstance() //conexión directa a nuestra base de datos
    val patientWithId = db.collection(M_C_P).document(idP)
}