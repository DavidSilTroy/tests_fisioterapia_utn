package com.example.tests_fisioterapia.UI.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.MainControl
import com.example.tests_fisioterapia.controllers.ShowMessages
import com.example.tests_fisioterapia.controllers.capitalizeFirstLetter
import com.example.tests_fisioterapia.network.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception
import java.text.SimpleDateFormat

class EditPatientActivity : AppCompatActivity() {

    //VARIABLES GLOBALES
    lateinit var databaseEdit : EditPatientDB
    lateinit var databaseData : PatientDBData

    lateinit var message: ShowMessages
    lateinit var user : String
    lateinit var idP : String

    var dbDataList : MutableMap<String,Any> = hashMapOf()
    var dataETList : MutableMap<String,Any> = hashMapOf()
    var selectedPhtoUri: Uri? = null

    var patientsId = listOf<String>()                   //para guardar todas las ids de los pacientes
    var patientName = ""
    var position = 0

    //FUNCIONES PROPIAS DE ANDROID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_patient)
        idP =  intent.getStringExtra("patientId")
        user = intent.getStringExtra("userloged")
        databaseEdit = EditPatientDB(idP,user)
        databaseData = PatientDBData(idP)
        message = ShowMessages(this)
        getDBData()
        setPhotoToIV()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhtoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhtoUri)
            val BitmapDrawable = BitmapDrawable(bitmap)
            val img_patient = findViewById<ImageView>(R.id.iv_edit_picture_patient)
            img_patient.setImageBitmap(BitmapDrawable.toBitmap())
        }
    }

    //FUNCIONES PARA LA ACTIVITY
    fun goToMain(){
        val intent = Intent(this, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finishAfterTransition()
        startActivity(intent)

    }                               /**Ir a la actividad principal y borrar todas las pestañas abiertas antes**/
    fun selectPicture(){
        val intent  =   Intent(Intent.ACTION_PICK)
        intent.type =   "image/*"

        startActivityForResult(intent, 0)
    }                          /**Le pedimos al usuario que seleccione una imagen**/
    fun getDBData(){
        databaseData.patientData.get()
                .addOnCompleteListener{
                    val name                = it.result?.data!!.get("name").toString()
                    val lastname            = it.result?.data!!.get("last_name").toString()
                    val age                 = it.result?.data!!.get("age").toString()
                    val gender              = it.result?.data!!.get("gender").toString()
                    val weight              = it.result?.data!!.get("weight").toString()
                    val height              = it.result?.data!!.get("height").toString()
                    val email               = it.result?.data!!.get("email").toString()
                    val diagnosis           = it.result?.data!!.get("diagnosis").toString()
                    val importantComments   = it.result?.data!!.get("important_comments").toString()

                    dbDataList = hashMapOf(
                            "name"                  to name.capitalizeFirstLetter(),
                            "last_name"             to lastname.capitalizeFirstLetter(),
                            "age"                   to age,
                            "gender"                to gender,
                            "weight"                to weight,
                            "height"                to height,
                            "email"                 to email,
                            "diagnosis"             to diagnosis,
                            "important_comments"    to importantComments
                    )/**Guardamos los datos en una lista con llave de tipo string**/
                    showDataET()/**Mostramos los datos en pantalla**/
                }
                .addOnCanceledListener {
                    message.show("algo salió mal")
                }
    }                              /**Buscamos los datos en la base de datos para mostrarlos**/
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

        patientName = "$name $lastname" /**Nombre completo del paciente**/
    }                             /**Mostramos los datos en la pantalla**/
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
            name.isBlank()      -> msj = "El nombre no puede quedar vacío"
            lastname.isBlank()  -> msj = "El apellido no puede quedar vacío"
            age.isBlank()       -> msj = "La edad no puede quedar vacío"
            gender.isBlank()    -> msj = "El sexo no puede quedar vacío"
            weight.isBlank()    -> msj = "El peso no puede quedar vacío"
            height.isBlank()    -> msj = "La altura no puede quedar vacío"
            else ->{
                dataETList= hashMapOf(
                        "name"                  to name.capitalizeFirstLetter(),
                        "last_name"             to lastname.capitalizeFirstLetter(),
                        "age"                   to age,
                        "gender"                to gender,
                        "weight"                to weight,
                        "height"                to height,
                        "email"                 to email,
                        "diagnosis"             to diagnosis,
                        "important_comments"    to importantComments
                )/**Guardamos los datos en una lista con llave de tipo string**/
                return true
            }
        }           /**Nos aseguramos que los campos no queden vacíos**/
        message.show(msj)   /**Mostramos un mensaje en caso de que un campo está vacío**/
        return false

    }                      /**Verificamos que los datos no estén vacios y se guardan en una lista**/
    fun saveData(){
        val registerList : MutableMap<String,Any> = hashMapOf()
        if (getETData()){
            val keys        = listOf(
                    "name",
                    "last_name",
                    "age",
                    "gender",
                    "weight",
                    "height",
                    "email",
                    "diagnosis",
                    "important_comments"
            ) /**llaves para las listas**/
            var haschanged  = false      /**Para saber si los datos obtenidos son los mismos a los que se pusieron al inicio**/
            val changesList = listOf<String>().toMutableList() /**Para poner el lista los datos que sí se cambiaron**/

            for(k in keys){
                if(dbDataList[k]!! != dataETList[k]) {
                    changesList.add(k)/**Se agrega la llave del datos que cambió**/
                    haschanged = true
                } /**En caso de que los datos obtenidos con los datos mostrados sean distintos**/
            }

            if(haschanged){
                for (i in changesList){
                    registerList[i]= dataETList[i] as Any
                }/**Se guardan en una lista los datos que se van a actualizar**/

                val date                    = System.currentTimeMillis() //tiempo actual en millisegundos
                val dateString: String      = SimpleDateFormat(PATTERN_DATE).format(date)
                dataETList["edited"]        = dateString
                registerList["edited"]      = dateString
                registerList["edited_by"]   = user

                databaseEdit.patienData.update(dataETList)/**Grabamos los datos**/
                        .addOnCompleteListener {
                            databaseEdit.register.document("r$date").set(registerList )
                                    .addOnCompleteListener {
                                        goToMain()
                                    }
                                    .addOnFailureListener {
                                        message.show("Algo salió mal al guardar el registro..")
                                    }
                        }
                        .addOnFailureListener {
                            message.show("Algo salió mal al guardar los datos..")
                        }
            }/**En caso de que se haya realizado un cambio en los textos*/
            else{
                if(selectedPhtoUri != null){
                    message.show("Guardando foto")
                    goToMain()
                }else{
                    message.show("No se han hecho cambios")
                    this.onBackPressed()
                }
            }
        }/**Si los datos importantes no están vacios**/
    }                               /**Guardamos los datos en caso de que se hayan realizado cambios**/
    fun getThePosition(): Int {
        val end = patientsId.size-1
        for (i in 0..end){
            if(patientsId[i]==idP) return i
        }
        return -1 //esto creará un error xd hay que corregir o controlar
    }                   /**Obtengo la posición del paciente en la lista de pacientes del usuario**/
    fun getTheIds(documents: Map<String, Any>){

        val idPatientsList = documents.keys.toList()

        if(idPatientsList.size>0){

            patientsId = listOf()
            val pIdColector = patientsId.toMutableList()

            for(i in 0..idPatientsList.size-1){
                val data = documents.get(idPatientsList[i])!!.toString()
                pIdColector.add(data)
            }

            patientsId = pIdColector.toList() /**Se guardan las ids**/
            position = getThePosition() /**Obtenemos la posición en la lista de el paciente**/

        }/**La lista debe tener al menos 1 item**/
        else{
            message.show("No se encuentra ningún paciente.. espera kha? xd") /**Este mensaje no debería verse jajaja**/
            goToMain()
        }
    }   /**Se obtienen todas las ids de los pacientes que tiene el usuario**/
    fun delet_patient(){
        val loadingEreasing         = findViewById<RelativeLayout>(R.id.layout_ereasing_patient_edit)
        loadingEreasing.visibility  = View.VISIBLE /**Mostramos una pantalla de carga**/

        databaseEdit.userPatient.get()
                .addOnCompleteListener {

                    if(it.result?.data.isNullOrEmpty()){
                        message.show("Error al encontrar el usuario a borrar, no hay datos")
                    }else{
                        val documents       = it.result?.data
                        val idPatientsList  = documents?.keys?.toList()!!

                        getTheIds(documents)

                        //eleminando el valor específico
                        databaseEdit.userPatient
                                .update(hashMapOf(idPatientsList[position] to FieldValue.delete()) as Map<String, Any>)
                                .addOnCompleteListener{

                                    message.show("Borrando a $patientName")

                                    Handler().postDelayed({
                                        goToMain()
                                    }, 1500)
                                }
                                .addOnCanceledListener {
                                    message.show("no se pudo borrar el paciente")
                                    loadingEreasing.visibility = View.GONE
                                }
                    }
                }
                .addOnFailureListener {
                    message.show("Algo salió mal, revisa tu internet y vuelve a intentarlo")
                    loadingEreasing.visibility = View.GONE
                }
    }                          /**Borramos al paciente de la lista de pacientes del usuario**/
    fun setPhotoToIV(){

        val storage         = Firebase.storage
        val storageRef      = storage.reference
        val patientImgRef   = storageRef.child("images/$idP")
        val imageView       = findViewById<CircleImageView>(R.id.iv_edit_picture_patient)
        val MAX_BYTES: Long = 10485760 //10Mb
        val progresbarPhoto = findViewById<ProgressBar>(R.id.pb_edit_photo_patient)

        try {
            patientImgRef.getBytes(MAX_BYTES).addOnSuccessListener {

                val bitmap          = BitmapFactory.decodeByteArray(it,0,it.size)
                val BitmapDrawable  = BitmapDrawable(bitmap)

                imageView.setImageBitmap(BitmapDrawable.toBitmap())
                progresbarPhoto.visibility = View.GONE

            }.addOnFailureListener {
                progresbarPhoto.visibility = View.GONE
            }

        } catch (e: Exception) {
            progresbarPhoto.visibility = View.GONE
        }
    }                           /**Se obtiene la foto del paciente si es que existe una en la base de datos**/
    fun uploadPatienImage(){
        message.show("Revisando los datos..")

        val filename = idP
        val ref      = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhtoUri!!).addOnCompleteListener{
            saveData()
        }
    }                      /**Se guarda la foto del paciente**/

    //BOTONES
    fun btn_editPatientPhoto(view: View) {
        MainControl().buttonEffect(view)
        selectPicture()
    }   /**Botón de agregar foto**/
    fun btn_save_editPatient(view: View) {
        MainControl().buttonEffect(view)
        message.show("Guardando")
        if(selectedPhtoUri!= null){
            uploadPatienImage()
        }/**En caso de haber agregado foto esta se subirá primero y después los datos**/
        else{
            saveData()
        }
    }   /**Botón de agregar datos editados**/
    fun btn_delete_patient(view: View) {
        MainControl().buttonEffect(view)
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Paciente")
        builder.setMessage("Confirme que desea ELIMINAR a $patientName")
        builder.setPositiveButton("Confirmar"){ dialogInterface: DialogInterface, i: Int ->
            delet_patient()
        }
        builder.setNeutralButton("Cancelar") { dialogInterface: DialogInterface, i: Int ->
        }
        builder.show()
    }     /**Botón de eliminar el paciente de la lista del usuario**/

}