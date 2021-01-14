package com.example.tests_fisioterapia.UI.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.*
import com.example.tests_fisioterapia.network.M_C_P
import com.example.tests_fisioterapia.network.M_C_UP
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    /** Aquí están las variables globales del programa**/
    var user : String = ""                              //para guardar el usuario y mostrarlo
    var dbDataSize : Int= 0                             //para obtener el tamaño de la base de datos de pacientes
    var patients = listOf<PatientsData>()               //para guardar los datos a mostrar de cada paciente
    var patientsId = listOf<String>()                   //para guardar todas las ids de los pacientes
    private lateinit var auth: FirebaseAuth             //para la autenticación de firebase
    private val db = FirebaseFirestore.getInstance()    //para la base de datos
    var isAdding = false                                //al pasar a la activity de agregar se vuelve true

    /**Estas son variables que ya no se están usando por ahora, las borraremos al final**/
    //val timeLoading = LoadingTime().DBtime()
    //var progressbarStatus = 0
    //var patientsCount = 1
    //var unidad = 0
    //var itemsShowed = 10
    //var isLoading = true

    /**Función por defecto en  de Android**/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /**Recuerden que el layout debe estar en el AndroidManifest para que pueda ser abierto,
        así <activity android:name=".UI.activities.MainActivity"/> **/

        /** Obteniendo datos del activity de login o de la autenticación si ya inició sesión antes **/
        auth = Firebase.auth

    }

    override fun onStart(){
        /** Obtenemos el usuario de la autenticación si es que no viene de la activity de login**/
        user = if(intent.hasExtra("userloged")){
            intent.getStringExtra("userloged")
            //Toast.makeText(applicationContext, "Hola $user" , Toast.LENGTH_LONG).show()
        }else{
            val currentUser = auth.currentUser
            val end = currentUser!!.email!!.indexOf("@")
            currentUser.email!!.substring(0,end)
        }
        /** Ubicando el nombre del usuario**/
        findViewById<TextView>(R.id.tv_user_name).text = user
        /**Vaciamos la lista en caso de que hayan quedado datos guardados**/
        patients= listOf()
        /** Desplegamos que está cargando**/
        findViewById<RelativeLayout>(R.id.layout_loading_patients).visibility = View.VISIBLE
        /** Obtenemos las ids de los pacientes del usuario y los datos para mostrar**/
        getPatientsId()
        super.onStart()
    }
    override fun onResume() {

        val email = auth.currentUser?.isEmailVerified!!
        if(email){
            findViewById<RelativeLayout>(R.id.layout_verified_user).visibility = View.GONE
        }
        else{
            Toast.makeText(baseContext,
                    "Debes verificar tu correo, revisa en el area de Spam o Junk Email $email y ${auth.currentUser?.email}",
                    Toast.LENGTH_LONG).show()
            auth.signOut()
        }

        super.onResume()
    }

    /**###### FUNCIONES CREADAS ######**/
    /**Función para mostrar los datos de los pacientes**/
    fun initRecyclerPatients(){
        /**Guardamos la id de los Views para ubicar los datos**/
        val rvPatients = findViewById<RecyclerView>(R.id.rv_patients_show)
        val layoutLoading = findViewById<RelativeLayout>(R.id.layout_loading_more_patients)
        /****/
        val adapter = PatientAdapter(patients,this)
        /****/
        val layoutManager = LinearLayoutManager(this)
        /****/
        rvPatients.layoutManager =layoutManager
        /****/
        MainControl().showRecyclerPatients(layoutLoading, rvPatients, adapter)

    }
    /**Función para obtener los datos de la base de datos de los pacientes**/
    fun getDataDBtoList(startId : Int =0){
        val endId = patientsId.size-1
        val lista = patients.toMutableList()

        db.collection(M_C_P).document(patientsId[startId]).get()
                .addOnCompleteListener{
                    if (it.result?.data.isNullOrEmpty()){
                        Toast.makeText(applicationContext,
                                "No se encontraron los datos ${patientsId[startId]}"
                                , Toast.LENGTH_LONG).show()
                        findViewById<RelativeLayout>(R.id.layout_loading_patients).visibility = View.GONE
                    }else{

                        val name = it.result?.data!!.get("name").toString()
                        val lastname = it.result?.data!!.get("last_name").toString()
                        val gender = it.result?.data!!.get("gender").toString().substring(0,1)
                        val age = it.result?.data!!.get("age").toString()
                        val height = it.result?.data!!.get("height").toString()
                        val weight = it.result?.data!!.get("weight").toString()
                        val diagnosis = it.result?.data!!.get("diagnosis").toString()
                        val edited = it.result?.data!!.get("edited").toString()
                        lista.add(
                                PatientsData(
                                        nametoShow(name,lastname),
                                        age,
                                        gender,
                                        height,
                                        weight,
                                        diagnosis,
                                        edited,
                                        patientsId[startId]
                                )
                        )
                        patients = lista.toList()
                        if(startId>=endId){
                            findViewById<RelativeLayout>(R.id.layout_loading_patients).visibility = View.GONE
                            initRecyclerPatients()
                        }else{
                            findViewById<ProgressBar>(R.id.pb_h_loading_patients).progress +=(100/patientsId.size)
                            getDataDBtoList(startId+1)
                        }
                    }

                }
                .addOnCanceledListener {
                    Toast.makeText(applicationContext,
                            "algo salió mal"
                            , Toast.LENGTH_LONG).show()
                }

    }
    /**Función para obtener las ids de los pacientes en la base de datos**/
    fun getPatientsId(){
        db.collection(M_C_UP).document(user).get()
                .addOnCompleteListener {
                    if(it.result?.data.isNullOrEmpty()){
                        findViewById<TextView>(R.id.tv_add_your_first_patient).visibility = View.VISIBLE
                        findViewById<RelativeLayout>(R.id.layout_loading_patients).visibility = View.GONE
                    }else{
                        val documents = it.result?.data
                        val idPatientsList = documents?.keys?.toList()!!
                        dbDataSize = idPatientsList.size
                        if(idPatientsList.isNotEmpty()){
                            patientsId = listOf()
                            val pIdColector = patientsId.toMutableList()
                            for(i in 0..dbDataSize-1){
                                val data = it.result?.data?.get(idPatientsList[i])!!.toString()
                                pIdColector.add(data)
                            }
                            patientsId = pIdColector.toList()
                            getDataDBtoList()
                        }else{
                            findViewById<TextView>(R.id.tv_add_your_first_patient).visibility = View.VISIBLE
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext,
                            "Algo salió mal, revisa tu internet y vuelve a intentarlo" ,
                            Toast.LENGTH_LONG).show()
                }

    }
    /**Función para unir pirmer nombre y primer apellido si existen 2 de cada uno**/
    fun nametoShow(name: String, lastname: String): String {
        var name = name
        var lastname = lastname
        if (name.contains(" ")){
            val endname = name.indexOf(" ")
            name = name.substring(0,endname)
        }

        if (lastname.contains(" ")){
            val endname = lastname.indexOf(" ")
            lastname = lastname.substring(0,endname)
        }
        var completeName = "$name $lastname"
        val maxSize = 12
        if(completeName.length>maxSize){
            completeName = "${ completeName.substring(0, maxSize)}.."
        }
        return  completeName
    }

    /**###### FUNCIONES PARA BOTONES ######**/
    fun btn_OpenUserMenu(view: View){
        //Toast.makeText(applicationContext, "Botonazo de menu", Toast.LENGTH_SHORT).show()
        val the_menu = findViewById<LinearLayout>(R.id.layout_user_menu)
        if (the_menu.visibility == View.GONE){
            the_menu.visibility = View.VISIBLE
            the_menu.bringToFront()
        }else {the_menu.visibility = View.GONE}
    }
    fun userOptionsTouched(view: View){
        val txt_option = view.findViewById<TextView>(view.id)
        when(txt_option.text.toString()){
            "Cerrar Sesión" -> {
                Toast.makeText(applicationContext, "Cerrando sesión..", Toast.LENGTH_LONG).show()
                auth.signOut()
                this.finishAfterTransition()
                this.onBackPressed()
                //startActivity(Intent(this, IndexActivity::class.java))

            }
            "Editar Usuario"->{
                val intent = Intent(this, AddUserDataActivity::class.java)
                startActivity(intent)
                this.finishAfterTransition()
            }
            else ->Toast.makeText(
                applicationContext,
                "ejecutando ${txt_option.text}... aún no programado",
                Toast.LENGTH_LONG
            ).show()
        }

    }
    fun btn_addNewPatient(view: View){
        isAdding = true
        //this.finishAfterTransition()
        view.visibility = View.INVISIBLE
        val intent = Intent(this, AddPatientActivity::class.java).apply{
            putExtra("userloged", user)
        }
        startActivity(intent)
    }


}