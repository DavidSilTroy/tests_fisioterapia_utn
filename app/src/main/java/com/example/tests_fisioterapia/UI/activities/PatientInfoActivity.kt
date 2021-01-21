package com.example.tests_fisioterapia.UI.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.network.M_C_P
import com.google.firebase.firestore.FirebaseFirestore

import android.os.Environment
import android.os.Handler
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.controllers.*
import com.example.tests_fisioterapia.network.GetPatientDB
import com.example.tests_fisioterapia.network.GetTestsPatientInfo
import com.itextpdf.text.Document
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class PatientInfoActivity : AppCompatActivity() {

    //private val db = FirebaseFirestore.getInstance()    //para la base de datos
    lateinit var databaseTests : GetTestsPatientInfo
    lateinit var databasePatient : GetPatientDB
    private var idPatient : String = ""
    private val STORAGE_CODE: Int = 100;
    lateinit var dataList : MutableMap<String,String>
    var testsInfolist = listOf<testInfoData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_info)
        //Toast.makeText(applicationContext, patients.size.toString(), Toast.LENGTH_LONG).show()

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
        if(intent.hasExtra("patientId")){
            idPatient = intent.getStringExtra("patientId")
            databasePatient = GetPatientDB(idPatient)
            databaseTests = GetTestsPatientInfo(idPatient)
            getDataDB()
        }else{
            this.onBackPressed()
        }
        super.onResume()
    }

    fun showMsg(message:String){
        Toast.makeText(applicationContext,
            message
            , Toast.LENGTH_LONG).show()
    }

    /**Función para obtener los datos de la base de datos de los pacientes y mostrarla**/
    fun getDataDB() {
        databasePatient.patientWithId.get()
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
    }
    fun createPatientReport(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            //system OS >= Marshmallow(6.0), check permission is enabled or not
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                //permission was not granted, request it
                val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                requestPermissions(permissions, STORAGE_CODE)
            }
            else{
                //permission already granted, call savePdf() method
                savePdf()
            }
        }
        else{
            //system OS < marshmallow, call savePdf() method
            savePdf()
        }
    }
    private fun savePdf() {

        //get text from EditText i.e. textEt
        val name = dataList["name"]
        val age = dataList["age"]
        val weight =dataList["weight"]
        val height =dataList["height"]
        val diagnosis =dataList["diagnosis"]
        val importantComments = dataList["importantComments"]
        //var gender = dataList["gender"]

        //create object of Document class
        val mDoc = Document()
        //pdf file name
        val mFileName = "Información Básica de $name"
        //pdf file path
        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/" + mFileName +".pdf"
        try {
            //create instance of PdfWriter class
            PdfWriter.getInstance(mDoc, FileOutputStream(mFilePath))

            //open the document for writing
            mDoc.open()

            //add author of the document (metadata)
            mDoc.addAuthor("Fisioterapia App")

            //add paragraph to the document
            mDoc.add(Paragraph("El nombre del paciente es: $name"))
            mDoc.add(Paragraph("La edad es: $age años"))
            mDoc.add(Paragraph("El peso es: $weight [kg]"))
            mDoc.add(Paragraph("La altura es: $height [m]"))
            mDoc.add(Paragraph("Diagnostico: $diagnosis"))
            mDoc.add(Paragraph("Comentarios importantes: $importantComments"))

            //close document
            mDoc.close()
            //show file saved message with file name and path
            Toast.makeText(this, "$mFileName.pdf\n se ha guardado en \n tu dispositivo", Toast.LENGTH_LONG).show()
        }
        catch (e: Exception){
            //if anything goes wrong causing exception, get and show exception message
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
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
                            testsList.add(testInfoData(testName,result,created,testId,testPosition,idPatient))
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            STORAGE_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted, call savePdf() method
                    savePdf()
                }
                else{
                    //permission from popup was denied, show error message
                    Toast.makeText(this, "Permission denied...!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun btn_powered_action(view: View) {
        val intent = Intent(this, PoweredByActivity::class.java)
        startActivity(intent)
    }
    fun btn_addNewTest(view: View) {
        Toast.makeText(applicationContext, "Agregar un nuevo test", Toast.LENGTH_LONG).show()
        view.visibility = View.INVISIBLE
        val intent = Intent(this, AddTestActivity::class.java).apply{
            putExtra("patientId", idPatient)
        }
        startActivity(intent)
        view.visibility = View.VISIBLE
        testsInfolist = listOf()
    }
    fun btn_editPatient(view: View) {
        view.visibility = View.INVISIBLE

        val intent = Intent(this, EditPatientActivity::class.java).apply{
            putExtra("patientId", idPatient)
        }
        startActivity(intent)
        view.visibility = View.VISIBLE
        this.finishAfterTransition()

    }

}