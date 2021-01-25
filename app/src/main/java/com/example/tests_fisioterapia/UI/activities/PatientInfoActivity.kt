package com.example.tests_fisioterapia.UI.activities


import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import android.os.Handler
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.controllers.*
import com.example.tests_fisioterapia.network.GetTestsPatientInfo
import com.example.tests_fisioterapia.network.PatientDBData
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception


class PatientInfoActivity : AppCompatActivity() {

    lateinit var databaseTests : GetTestsPatientInfo
    lateinit var databasePatient : PatientDBData
    private var idPatient : String = ""
    private var user : String = ""
    lateinit var dataList : MutableMap<String,String>
    var testsInfolist = listOf<testInfoData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_info)
        //Toast.makeText(applicationContext, patients.size.toString(), Toast.LENGTH_LONG).show()
        if(intent.hasExtra("patientId")){
            idPatient = intent.getStringExtra("patientId")
            user = intent.getStringExtra("userloged")
            databasePatient = PatientDBData(idPatient)
            databaseTests = GetTestsPatientInfo(idPatient)

        }else{
            this.onBackPressed()
        }
    }
    override fun onResume() {
        findViewById<TextView>(R.id.tv_diagnosis_title_info_patient).setOnClickListener{
            //Toast.makeText(applicationContext, "titulo de diagnostico", Toast.LENGTH_LONG).show()
        }
        findViewById<TextView>(R.id.tv_diagnosis_info_patient).setOnClickListener{
            //Toast.makeText(applicationContext, "el diagnostico", Toast.LENGTH_LONG).show()
        }
        findViewById<TextView>(R.id.tv_important_commets_info).setOnClickListener{
            //Toast.makeText(applicationContext, "los comentarios", Toast.LENGTH_LONG).show()
        }
        testsInfolist = listOf()
        getDataDB()
        super.onResume()
    }


    fun showMsg(message:String){
        Toast.makeText(applicationContext,
            message
            , Toast.LENGTH_LONG).show()
    }

    /**Función para obtener los datos de la base de datos de los pacientes y mostrarla**/
    fun getDataDB() {
        databasePatient.patientData.get()
            .addOnCompleteListener{
                if (it.result?.data.isNullOrEmpty()){
                    this.onBackPressed()
                }else{
                    val name = "${it.result?.data!!.get("name")} ${it.result?.data!!.get("last_name")}"
                    val age = it.result?.data!!.get("age").toString()
                    val weight = it.result?.data!!.get("weight").toString()
                    val height = it.result?.data!!.get("height").toString()
                    val diagnosis = it.result?.data!!.get("diagnosis").toString()
                    var importantComments = it.result?.data!!.get("important_comments").toString()
                    var gender = it.result?.data!!.get("gender").toString()
                    if(importantComments == "null" || importantComments.isBlank()){
                        importantComments = "Agrega un comentario importante sobre este paciente"
                    }
                    if(gender.length>3){
                        gender = gender.substring(0,4)
                    }

                    findViewById<TextView>(R.id.tv_name_patient_info).text =name
                    findViewById<TextView>(R.id.tv_age_info_patient).text ="$age años"
                    findViewById<TextView>(R.id.tv_gender_info_patient).text =gender
                    findViewById<TextView>(R.id.tv_weight_info_patient).text ="$weight kg"
                    findViewById<TextView>(R.id.tv_diagnosis_info_patient).text =diagnosis
                    findViewById<TextView>(R.id.tv_important_commets_info).text =importantComments

                    dataList = hashMapOf()
                    dataList["name"]=name
                    dataList["age"]=age
                    dataList["gender"]=gender
                    dataList["weight"]=weight
                    dataList["height"]=height
                    dataList["diagnosis"]=diagnosis
                    dataList["importantComments"]=importantComments
                    GetTestDataDB()
                }

            }
            .addOnCanceledListener {
                Toast.makeText(applicationContext,
                    "algo salió mal"
                    , Toast.LENGTH_LONG).show()
            }

        setPhotoToIV()




    }


    fun setPhotoToIV(){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val patientImgRef = storageRef.child("images/$idPatient")
        val imageView = findViewById<CircleImageView>(R.id.iv_patient_photo_info)
        val MAX_BYTES: Long = 10485760 //10Mb
        val progresbarPhoto = findViewById<ProgressBar>(R.id.pb_photo_patient_info)
        val noPhotoText = findViewById<TextView>(R.id.tv_no_photo_patient_info)

        try {
            patientImgRef.getBytes(MAX_BYTES).addOnSuccessListener {
                // Data for "images/island.jpg" is returned, use this as needed
                //showMsg("bien en getBytes ${it}")
                val bitmap = BitmapFactory.decodeByteArray(it,0,it.size)
                val BitmapDrawable = BitmapDrawable(bitmap)
                imageView.setImageBitmap(BitmapDrawable.toBitmap())
                progresbarPhoto.visibility = View.GONE
            }.addOnFailureListener {
                //showMsg("Algo falló en getBytes ${it.message}")
                // Handle any errors
                progresbarPhoto.visibility = View.GONE
                noPhotoText.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            progresbarPhoto.visibility = View.GONE
            noPhotoText.visibility = View.VISIBLE
        }


    }
    fun GetTestDataDB(){
        databaseTests.testsCollection.get().addOnCompleteListener {

            if(it.result?.data.isNullOrEmpty()){
                initRecyclerTests()
                findViewById<TextView>(R.id.tv_no_test_exist_patient_info).visibility = View.VISIBLE
            }else{

                val testsList = testsInfolist.toMutableList()
                val data = it.result!!.data!!
                val listSize = it.result!!.data!!.size
                val datakeyList = data.keys
                var count = 1
                for(i in datakeyList){
                    val testPosition = i
                    val testId = data.get(testPosition).toString()

                    databaseTests.testInfo.document(testId).get().addOnCompleteListener {
                        if(it.result?.data.isNullOrEmpty()){
                            showMsg("Algo salió mal..")
                        }else{
                            val testData = it.result?.data!!
                            val created = testData.get("created").toString()
                            val result = testData.get("total_result").toString()
                            val testName = testData.get("test_name").toString()
                            testsList.add(testInfoData(testName,result,created,testId,testPosition,idPatient,user))
                            if(count==listSize){
                                testsInfolist = testsList.toList()
                                initRecyclerTests()
                            }
                            count+=1
                        }

                    }
                }


            }
        }

    }
    fun initRecyclerTests(){
        /**Guardamos la id de los Views para ubicar los datos**/
        val rvTestsInfo = findViewById<RecyclerView>(R.id.rv_tests_patient_info)
        val pbLoading = findViewById<ProgressBar>(R.id.pb_loading_tests_patient_info)
        /****/
        val adapter = TestInfoAdapter(testsInfolist,this)
        /****/
        val layoutManager = LinearLayoutManager(this)
        /****/
        rvTestsInfo.layoutManager =layoutManager
        /****/
        MainControl().showRecyclerTestsInfo(pbLoading, rvTestsInfo, adapter)
    }


    fun btn_powered_action(view: View) {
        val intent = Intent(this, PoweredByActivity::class.java)
        startActivity(intent)
    }
    fun btn_addNewTest(view: View) {
        view.alpha = 0.5f
        Handler().postDelayed({
            view.alpha = 1f
        }, 800)
        Toast.makeText(applicationContext, "Agregar un nuevo test", Toast.LENGTH_LONG).show()
        val intent = Intent(this, AddTestActivity::class.java).apply{
            putExtra("patientId", idPatient)
        }
        startActivity(intent)
        testsInfolist = listOf()
    }
    fun btn_editPatient(view: View) {
        view.alpha = 0.5f
        Handler().postDelayed({
            view.alpha = 1f
        }, 800)
        val intent = Intent(this, EditPatientActivity::class.java).apply{
            putExtra("patientId", idPatient)
            putExtra("userloged",user)
        }
        startActivity(intent)
        this.finishAfterTransition()

    }


}