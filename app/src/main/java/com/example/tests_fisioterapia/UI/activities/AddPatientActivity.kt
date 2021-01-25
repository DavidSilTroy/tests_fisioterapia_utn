package com.example.tests_fisioterapia.UI.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.capitalizeFirstLetter
import com.example.tests_fisioterapia.network.AddPatientDB
import com.example.tests_fisioterapia.network.M_C_UP
import com.example.tests_fisioterapia.network.PATTERN_DATE
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat

class AddPatientActivity : AppCompatActivity() {


    lateinit private var databaseAdd  : AddPatientDB
    lateinit private var btn_add_view : View
    lateinit private var pb_add       : ProgressBar

    lateinit var newPatient : Map<String,Any>

    private var selectedPhtoUri: Uri? = null

    private val date = System.currentTimeMillis() //tiempo actual en millisegundos

    private var fistPatient = true
    private var dateString  = ""
    private var number      = 1
    private var user        = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_patient)
        if(intent.hasExtra("userloged")){
            user = intent.getStringExtra("userloged")
        }else {
            this.onBackPressed()
            this.finishAfterTransition()
        }
        databaseAdd = AddPatientDB(user,applicationContext,date)
    }
    override fun onResume(){
        if (user.isBlank()){
            this.finishAfterTransition()
            this.onBackPressed()
        }else{
            getNumberofPatient()
        }
        super.onResume()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhtoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhtoUri)
            val BitmapDrawable = BitmapDrawable(bitmap)
            val img_patient = findViewById<ImageView>(R.id.iv_add_picture_patient)
            img_patient.setImageBitmap(BitmapDrawable.toBitmap())


        }
    }


    /**Funciones de la activity**/
    private fun showMsg(message:String){
        Toast.makeText(applicationContext,
                message
                , Toast.LENGTH_LONG).show()
    }
    @SuppressLint("SimpleDateFormat")
    private fun getETdata(){
        val name =      findViewById<EditText>(R.id.et_add_name)?.text.toString()
        val lastname =  findViewById<EditText>(R.id.et_add_last_name)?.text.toString()
        val age =       findViewById<EditText>(R.id.et_add_age)?.text.toString()
        val gender =    findViewById<EditText>(R.id.et_add_gender)?.text.toString()
        val weight =    findViewById<EditText>(R.id.et_add_weight)?.text.toString()
        val height =    findViewById<EditText>(R.id.et_add_height)?.text.toString()
        val email =     findViewById<EditText>(R.id.et_add_email)?.text.toString()
        var msj = "Verificando datos..."
        when{
            name.isEmpty()     || name     == "null"    ->msj= "Falta el nombre"
            lastname.isEmpty() || lastname == "null"    ->msj= "Falta el apellido"
            age.isEmpty()      || age      == "null"    ->msj= "Falta la edad"
            gender.isEmpty()   || gender   == "null"    ->msj= "Falta el sexo"
            weight.isEmpty()   || weight   == "null"    ->msj= "Falta el peso"
            height.isEmpty()   || height   == "null"    ->msj= "Falta la altura"
            age.length>3                                ->msj= "Revisa la edad"
            else                                        ->{
                dateString= SimpleDateFormat(PATTERN_DATE).format(date)
                newPatient = hashMapOf(
                        "edited" to dateString,
                        "name" to name.capitalizeFirstLetter(),
                        "last_name" to lastname.capitalizeFirstLetter(),
                        "age" to age,
                        "gender" to gender.capitalizeFirstLetter(),
                        "weight" to  weight,
                        "height" to  height,
                        "email" to email,
                        "diagnosis" to "Sin Diagnostico",
                        "important_comments" to "Agrega un comentario"
                )
                savePatientData()
            }
        }
        Toast.makeText(applicationContext, msj , Toast.LENGTH_LONG).show()

        if (msj != "Verificando datos..."){
            Handler().postDelayed({
                btn_add_view.visibility = View.VISIBLE
                pb_add.visibility = View.GONE
                findViewById<ProgressBar>(R.id.pb_adding_patient).visibility = View.GONE
            }, 500)
        }

    }
    private fun uploadPatienImage(){
        Toast.makeText(applicationContext, "Revisando los datos.." , Toast.LENGTH_LONG).show()
        val filename = "p$date"
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhtoUri!!).addOnCompleteListener{
            showMsg("Paciente en la lista!!")
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.finishAfterTransition()
            startActivity(intent)
        }
    }

    /**DB*/
    private fun getNumberofPatient(){
        databaseAdd.patientToUser.get()
                .addOnSuccessListener {
                    if(it.data == null ){
                        databaseAdd.patientToUser.collection("register").document("main").set(hashMapOf("created" to dateString))
                    }else{
                        fistPatient = false
                        while(number<901){
                            val data = it.get("paciente_$number" as String).toString()
                            if(data=="null") break
                            number++
                        }
                    }
                    when(number){
                        100-> showMsg("Felicidades! tienes 100 pacientes ya, increíble!")
                        200-> showMsg("Felicidades! tienes 200 pacientes ya, asombroso!")
                        300-> showMsg("Espera! tienes 300 pacientes ya, wow! felicidades!")
                        400-> showMsg("Espera! tienes 400 pacientes ya, eres alguien increíble!")
                        500-> showMsg("Espera! tienes 500 pacientes! tú no eres humano! felicidades!")
                        600-> showMsg("No lo puedo creer.. tienes 600 pacientes! no te cansas? te admiro! felicidades")
                        700-> showMsg("No lo puedo creer.. tienes 700 pacientes! te sabes todos sus nombres? felicidades por tanto!")
                        800-> showMsg("Hey! tienes 800 pacientes! evidentemente eres la persona más pro de todas")
                        900-> showMsg("Súper felicidades! tienes 900 pacientes! pero.. me temo que  ya te quedas sin espacio...")
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Algo salió mal, intentalo mas tarde" , Toast.LENGTH_LONG).show()
                    this.onBackPressed()
                    this.finishAfterTransition()
                }
    }
    private fun savePatientData(){

        if(fistPatient){
            databaseAdd.patientToUser.set(
                    hashMapOf("paciente_$number" to "p$date")
            ).addOnCompleteListener {
                addPatientData()
            }.addOnFailureListener {
                showMsg("Ocurrio un error al agregar los datos..")
                this.onBackPressed()
                this.finishAfterTransition()
            }
        }else{
            databaseAdd.patientToUser.update(
                    "paciente_$number", "p$date"
            ).addOnCompleteListener {
                addPatientData()
            }.addOnFailureListener {
                showMsg("Ocurrio un error al agregar los datos..")
                this.onBackPressed()
                this.finishAfterTransition()
            }
        }
    }
    private fun addPatientData(){
        databaseAdd.patienData.set(newPatient).addOnCompleteListener {
            databaseAdd.patienRegister.set(mapOf(
                    "creation" to dateString,
                    "created_by" to user)
            ).addOnCompleteListener {
                if (selectedPhtoUri != null){
                    showMsg("Agregando foto..")
                    uploadPatienImage()
                }else{
                    showMsg("Paciente en la lista!!")
                    val intent = Intent(this, MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    this.finishAfterTransition()
                    startActivity(intent)
                }
            }.addOnFailureListener {
                showMsg("Error al guardar registros")
            }
        }.addOnFailureListener {
            showMsg("Error al agregar los datos del paciente..")
            this.onBackPressed()
            this.finishAfterTransition()
        }
    }

    /**Botones**/
    fun btn_addPatient(view: View){
        btn_add_view = view
        pb_add =findViewById<ProgressBar>(R.id.pb_adding_patient)
        view.visibility = View.INVISIBLE
        pb_add.visibility = View.VISIBLE
        getETdata()
    }
    fun btn_addPatientPhoto(view: View) {
        view.alpha = 0.5f
        Handler().postDelayed({
            view.alpha = 1f
        }, 800)
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)
    }

}