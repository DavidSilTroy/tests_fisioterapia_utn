package com.example.tests_fisioterapia.UI.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.capitalizeFirstLetter
import com.example.tests_fisioterapia.network.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class EditPatientActivity : AppCompatActivity() {

    lateinit var databaseEdit : EditPatientDB
    lateinit var databaseData : GetPatientDB
    var dbDataList : MutableMap<String,Any> = hashMapOf()
    var dataETList : MutableMap<String,Any> = hashMapOf()

    private lateinit var auth: FirebaseAuth             //para la autenticación de firebase
    private val db = FirebaseFirestore.getInstance()    //para la base de datos
    var patientsId = listOf<String>()                   //para guardar todas las ids de los pacientes
    lateinit var idP : String
    var patientName = ""
    lateinit var user : String
    var position = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_patient)
        //Toast.makeText(applicationContext, patients.size.toString(), Toast.LENGTH_LONG).show()

        //auth = Firebase.auth
        auth = Firebase.auth
        idP =  intent.getStringExtra("patientId")
        val currentUser = auth.currentUser
        val end = currentUser!!.email!!.indexOf("@")
        user = currentUser.email!!.substring(0,end)
        databaseEdit = EditPatientDB(idP,user,applicationContext)
        databaseData = GetPatientDB(idP)
        getDBData()
    }

    fun getDBData(){
        databaseData.patientWithId.get()
                .addOnCompleteListener{
                    val name = it.result?.data!!.get("name").toString()
                    val lastname = it.result?.data!!.get("last_name").toString()
                    val age = it.result?.data!!.get("age").toString()
                    val gender = it.result?.data!!.get("gender").toString()
                    val weight = it.result?.data!!.get("weight").toString()
                    val height = it.result?.data!!.get("height").toString()
                    val email = it.result?.data!!.get("email").toString()
                    val diagnosis = it.result?.data!!.get("diagnosis").toString()
                    val importantComments = it.result?.data!!.get("important_comments").toString()

                    dbDataList = hashMapOf(
                            "name" to name.capitalizeFirstLetter(),
                            "last_name" to lastname.capitalizeFirstLetter(),
                            "age" to age,
                            "gender" to gender,
                            "weight" to weight,
                            "height" to height,
                            "email" to email,
                            "diagnosis" to diagnosis,
                            "important_comments" to importantComments
                    )
                    showDataET()

                }
                .addOnCanceledListener {
                    Toast.makeText(this,
                            "algo salió mal"
                            , Toast.LENGTH_LONG).show()
                }
    }//Obtener datos de la Base de datos
    fun showDataET(){
            val name                = dbDataList["name"].toString()
            val lastname            = dbDataList["last_name"].toString()
            val age                 = dbDataList["age"].toString()
            val gender              = dbDataList["gender"].toString()
            val weight              = dbDataList["weight"].toString()
            val height              = dbDataList["height"].toString()
            val email               = dbDataList["email"].toString()
            val diagnosis           = dbDataList["diagnosis"].toString()
            val importantComments   = dbDataList["important_comments"].toString()

            findViewById<TextView>(R.id.et_edit_name).text                  = name
            findViewById<TextView>(R.id.et_edit_last_name).text             = lastname
            findViewById<TextView>(R.id.et_edit_age).text                   = age
            findViewById<TextView>(R.id.et_edit_gender).text                = gender
            findViewById<TextView>(R.id.et_edit_weight).text                = weight
            findViewById<TextView>(R.id.et_edit_height).text                = height
            findViewById<TextView>(R.id.et_edit_email).text                 = email
            findViewById<TextView>(R.id.et_edit_diagnosis).text             = diagnosis
            findViewById<TextView>(R.id.et_edit_important_comments).text    = importantComments
            patientName = "$name $lastname"
    }//Mostramos los datos que existen en la base de datos
    fun getETData():Boolean{
        val name                = findViewById<TextView>(R.id.et_edit_name).text.toString()
        val lastname            = findViewById<TextView>(R.id.et_edit_last_name).text.toString()
        val age                 = findViewById<TextView>(R.id.et_edit_age).text.toString()
        val gender              = findViewById<TextView>(R.id.et_edit_gender).text.toString()
        val weight              = findViewById<TextView>(R.id.et_edit_weight).text.toString()
        val height              = findViewById<TextView>(R.id.et_edit_height).text.toString()
        val email               = findViewById<TextView>(R.id.et_edit_email).text.toString()
        val diagnosis           = findViewById<TextView>(R.id.et_edit_diagnosis).text.toString()
        val importantComments   = findViewById<TextView>(R.id.et_edit_important_comments).text.toString()

        var msj = ""
        when{
            name.isBlank()-> msj = "El nombre no puede quedar vacío"
            lastname.isBlank()-> msj = "El apellido no puede quedar vacío"
            age.isBlank()-> msj = "La edad no puede quedar vacío"
            gender.isBlank()-> msj = "El sexo no puede quedar vacío"
            weight.isBlank()-> msj = "El peso no puede quedar vacío"
            height.isBlank()-> msj = "La altura no puede quedar vacío"
            else ->{
                dataETList= hashMapOf(
                        "name" to name.capitalizeFirstLetter(),
                        "last_name" to lastname.capitalizeFirstLetter(),
                        "age" to age,
                        "gender" to gender,
                        "weight" to weight,
                        "height" to height,
                        "email" to email,
                        "diagnosis" to diagnosis,
                        "important_comments" to importantComments
                )
                return true
            }
        }
        Toast.makeText(applicationContext, msj, Toast.LENGTH_LONG).show()
        return false

    }//obtener los datos escritos en un Map
    fun saveData(){
        val registerList : MutableMap<String,Any> = hashMapOf()
        if (getETData()){
            val keys = listOf(
                    "name",
                    "last_name",
                    "age",
                    "gender",
                    "weight",
                    "height",
                    "email",
                    "diagnosis",
                    "important_comments"
            )
            var haschanged = false
            val changesList = listOf<String>().toMutableList()

            for(k in keys){
                if(dbDataList[k]!! != dataETList[k]) {
                    changesList.add(k)
                    haschanged = true
                }
            }
            if(haschanged){
                for (i in changesList){
                    registerList[i]= dataETList[i] as Any
                }
                databaseEdit.saveData(dataETList)
                databaseEdit.saveRegister(registerList)
                this.finish()
                this.onBackPressed()
            }else{
                Toast.makeText(this,
                        "No se han hecho cambios"
                        , Toast.LENGTH_LONG).show()
            }
        }
    }

    fun delet_patient(){
        findViewById<RelativeLayout>(R.id.layout_ereasing_patient_edit).visibility = View.VISIBLE
        val currentUser = auth.currentUser
        val end = currentUser!!.email!!.indexOf("@")
        val user = currentUser.email!!.substring(0,end)


        db.collection(M_C_UP).document(user).get()
                .addOnCompleteListener {
                    if(it.result?.data.isNullOrEmpty()){
                        Toast.makeText(applicationContext,
                                "Eroor al encontrar el usuario a borrar.. no hay datos"
                                , Toast.LENGTH_LONG).show()
                    }else{
                        val documents = it.result?.data
                        val idPatientsList = documents?.keys?.toList()!!
                        getTheIds(documents)
                        position = getThePosition(documents)

                        //eleminando el valor específico
                        db.collection(M_C_UP).document(user)
                                .update(hashMapOf(idPatientsList[position] to FieldValue.delete()) as Map<String, Any>)
                                .addOnCompleteListener{
                                    Toast.makeText(applicationContext,
                                            "Borrando a $patientName"
                                            , Toast.LENGTH_LONG).show()
                                    Handler().postDelayed({
                                        this.onBackPressed()
                                        findViewById<RelativeLayout>(R.id.layout_ereasing_patient_edit).visibility = View.GONE
                                    }, 3000)

                                }
                                .addOnCanceledListener {
                                    Toast.makeText(applicationContext,
                                            "no se pudo borrar el paciente"
                                            , Toast.LENGTH_LONG).show()
                                }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext,
                            "Algo salió mal, revisa tu internet y vuelve a intentarlo" ,
                            Toast.LENGTH_LONG).show()
                }
        //findViewById<RelativeLayout>(R.id.layout_ereasing_patient_edit).visibility = View.GONE
    }
    fun getThePosition(documents: Map<String, Any>): Int {
        //Comparo las ids de los usuarios hasta encontrar la del paciente
        val end = patientsId.size-1
        for (i in 0..end){
         if(patientsId[i]==idP) return i
        }
        return -1
    }
    fun getTheIds(documents: Map<String, Any>) {
        //obtengo todas las ids de pacientes que tiene el usuario
        val idPatientsList = documents.keys.toList()
        if(idPatientsList.size>0){
            patientsId = listOf()
            val pIdColector = patientsId.toMutableList()
            for(i in 0..idPatientsList.size-1){
                val data = documents.get(idPatientsList[i])!!.toString()
                pIdColector.add(data)
            }
            patientsId = pIdColector.toList()
        }else{
            Toast.makeText(applicationContext,
                    "No se encuentran pacientes..."
                    , Toast.LENGTH_LONG).show()
        }
    }











    fun btn_editPatientPhoto(view: View) {
        Toast.makeText(applicationContext, "Esta opción estará lista en una próxima versión" , Toast.LENGTH_LONG).show()
    }
    fun btn_save_editPatient(view: View) {
        Toast.makeText(applicationContext, "Guardando" , Toast.LENGTH_LONG).show()
        view.visibility = View.INVISIBLE
        saveData()
        Handler().postDelayed({
            view.visibility = View.VISIBLE
        }, 1000)


}
    fun btn_delete_patient(view: View) {
        view.visibility = View.INVISIBLE
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Paciente")
        builder.setMessage("Confirme que desea ELIMINAR a $patientName")
        builder.setPositiveButton("Confirmar"){ dialogInterface: DialogInterface, i: Int ->
            delet_patient()
        }
        builder.setNeutralButton("Cancelar") { dialogInterface: DialogInterface, i: Int ->
        }
        builder.show()
        view.visibility = View.VISIBLE
    }

}