package com.example.tests_fisioterapia.UI.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.network.PatientDBData
import com.example.tests_fisioterapia.network.TestDBData
import com.example.tests_fisioterapia.network.UserDBData
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.TextAlignment
import com.itextpdf.layout.property.UnitValue
import java.io.File
import java.lang.Exception

class MakingReportActivity: AppCompatActivity() {

    private lateinit var dbPatient : PatientDBData
    private lateinit var dbUser : UserDBData
    private lateinit var dbTest : TestDBData
    private var documentData = listOf<String>()
    private var user = ""
    private var idPatient = ""
    private var idTest = ""
    private var testName = ""
    private val STORAGE_CODE: Int = 100;
    private var isUserData = false
    private var isPatientData = false
    private var isTestData = false
    private var isDocData = false
    private var isSavedDoc = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_making_report)
    }

    override fun onStart() {
        super.onStart()
        user =      intent.getStringExtra("userloged")
        idPatient = intent.getStringExtra("patientId")
        idTest=     intent.getStringExtra("testId")
        testName =  intent.getStringExtra("testName")

        findViewById<TextView>(R.id.tv_title_making_report).text = testName

        dbPatient = PatientDBData(idPatient)
        dbUser =    UserDBData(user)
        dbTest =    TestDBData(idTest)

        getUserData()
        //showMsg("user = $user ; idP = $idPatient ; idT = $idTest")
    }
    private fun showMsg(message:String){
        Toast.makeText(applicationContext,
                message
                , Toast.LENGTH_LONG).show()
    }

    private fun setCompleteTask(){
        if(isUserData)      findViewById<ImageView>(R.id.iv_option_1_making_report).setBackgroundResource(R.drawable.selected_circle_img)
        if(isPatientData)   findViewById<ImageView>(R.id.iv_option_2_making_report).setBackgroundResource(R.drawable.selected_circle_img)
        if(isTestData)      findViewById<ImageView>(R.id.iv_option_3_making_report).setBackgroundResource(R.drawable.selected_circle_img)
        if(isDocData)       findViewById<ImageView>(R.id.iv_option_4_making_report).setBackgroundResource(R.drawable.selected_circle_img)
        if(isSavedDoc)      findViewById<ImageView>(R.id.iv_option_5_making_report).setBackgroundResource(R.drawable.selected_circle_img)
    }
    private fun getUserData(){
        dbUser.userData.get().addOnCompleteListener {
            val name = it.result?.data?.get("name").toString()
            val last_name = it.result?.data?.get("last_name").toString()
            val semester = it.result?.data?.get("semester").toString()
            documentData = listOf(
                    name,
                    last_name,
                    semester
            )
            isUserData = true
            setCompleteTask()
            getPatienData()

        }
    }
    private fun getPatienData(){
        dbPatient.patientData.get().addOnCompleteListener {
            val name = it.result?.data?.get("name").toString()
            val last_name = it.result?.data?.get("last_name").toString()
            val age = it.result?.data?.get("age").toString()
            val weight = it.result?.data?.get("weight").toString()
            val height = it.result?.data?.get("height").toString()
            val diagnosis = it.result?.data?.get("diagnosis").toString()

            val listData = documentData.toMutableList()
            listData.add(name)
            listData.add(last_name)
            listData.add(age)
            listData.add(weight)
            listData.add(height)
            listData.add(diagnosis)
            documentData = listData.toList()
            isPatientData = true
            setCompleteTask()
            getTestData()
        }
    }
    private fun getTestData(){
        dbTest.testData.get().addOnCompleteListener {
            val test_name = it.result?.data?.get("test_name").toString()
            val total_result = it.result?.data?.get("total_result").toString()
            val created = it.result?.data?.get("created").toString()
            testName = test_name
            val listData = documentData.toMutableList()
            listData.add(test_name)
            listData.add(total_result)
            listData.add(created)

            documentData = listData.toList()
            isTestData = true
            setCompleteTask()
            Handler().postDelayed({
                createPatientReport()
            }, 500)

        }
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
                    Toast.makeText(this, "Permiso denegado para crear informes..", Toast.LENGTH_SHORT).show()
                }
            }
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
        CreateAppDirectory()
        //val date = System.currentTimeMillis()
        //val dateString = SimpleDateFormat("dd-MM-yyyy").format(date)
        val mFileName = "informe test de $testName"

        val mFilePath = Environment.getExternalStorageDirectory().toString() + "/fisioterapiaApp/Informes/" + mFileName +".pdf"
        val toShowPath = Environment.getExternalStorageDirectory().toString() + "/fisioterapiaApp/Informes/"

        val pdfDocument = PdfDocument(PdfWriter(mFilePath))

        //create object of Document class
        val mDoc = Document(pdfDocument)
        //pdf file name

        try {
            //add paragraph to the document
            val title = Paragraph("Universidad Técnica del Norte")
            title.setFontSize(20f)
            title.setFontColor(ColorConstants.RED)
            title.setTextAlignment(TextAlignment.CENTER)
            title.setBold()
            mDoc.add(title)

            var parag = Paragraph("Estudiante: ${documentData[0]} ${documentData[1]}")
            parag.setFontSize(14f)
            mDoc.add(parag)

            parag = Paragraph("Semestre: ${documentData[2]}")
            parag.setFontSize(14f)
            mDoc.add(parag)

            parag = Paragraph("")
            parag.setFontSize(14f)
            mDoc.add(parag)
            mDoc.add(parag)

            parag = Paragraph("Datos del Paciente")
            parag.setFontSize(16f)
            parag.setBold()
            mDoc.add(parag)

            parag = Paragraph("Nombre: ${documentData[3]} ${documentData[4]}")
            parag.setFontSize(14f)
            mDoc.add(parag)

            var table = Table(UnitValue.createPercentArray(floatArrayOf(8f, 23f, 15f, 15f, 12f, 12f, 15f))).useAllAvailableWidth()

            table.addCell(Cell().add(Paragraph("Edad:").setTextAlignment(TextAlignment.CENTER)))
            table.addCell(Cell().add(Paragraph(" ${documentData[5]} años")))
            mDoc.add(table)

            table = Table(UnitValue.createPercentArray(floatArrayOf(8f, 23f, 15f, 15f, 12f, 12f, 15f))).useAllAvailableWidth()
            table.addCell(Cell().add(Paragraph("Peso:").setTextAlignment(TextAlignment.CENTER)))
            table.addCell(Cell().add(Paragraph(" ${documentData[6]} kg")))
            mDoc.add(table)

            table = Table(UnitValue.createPercentArray(floatArrayOf(8f, 23f, 15f, 15f, 12f, 12f, 15f))).useAllAvailableWidth()
            table.addCell(Cell().add(Paragraph("Altura:").setTextAlignment(TextAlignment.CENTER)))
            table.addCell(Cell().add(Paragraph(" ${documentData[7]} m")))
            mDoc.add(table)

            parag = Paragraph("Diagnostico:")
            parag.setFontSize(14f)
            mDoc.add(parag)

            parag = Paragraph(documentData[8])
            parag.setFontSize(14f)
            mDoc.add(parag)

            parag = Paragraph("Test de ${documentData[9]}")
            parag.setFontSize(18f)
            parag.setFontColor(ColorConstants.BLUE)
            parag.setTextAlignment(TextAlignment.CENTER)
            parag.setBold()
            mDoc.add(parag)

            parag = Paragraph("Resultado del test: ${documentData[10]}")
            parag.setFontSize(14f)
            parag.setMargins(0f, 10f, 10f, 10f)
            mDoc.add(parag)

            parag = Paragraph("Test realizado: ${documentData[11]}")
            parag.setFontSize(14f)
            mDoc.add(parag)

            //close document
            mDoc.close()

            Handler().postDelayed({
                isDocData = true
                setCompleteTask()
            }, 500)

            //show file saved message with file name and path
            //Toast.makeText(this, "$mFileName.pdf\n se ha guardado en \n tu dispositivo", Toast.LENGTH_LONG).show()
            Handler().postDelayed({
                findViewById<TextView>(R.id.tv_result_location_report).text = "El  $mFileName.pdf\n se ha guardado en una carpeta llamada fisioterapiaApp \n en tu dispositivo"
                isSavedDoc = true
                setCompleteTask()
            }, 900)
        }
        catch (e: Exception){
            //if anything goes wrong causing exception, get and show exception message
            Toast.makeText(this, "Algo salió mal.. intentalo nuevamente después", Toast.LENGTH_LONG).show()

        }

    }

    fun CreateAppDirectory(){

        /** Para guardar los datos de los pdfs***/

        val sd_main = File(Environment.getExternalStorageDirectory(), "/fisioterapiaApp/Informes/")
        var success = true
        if (!sd_main.exists())
            success = sd_main.mkdir()

        if (success) {
            val sd = File("filename.txt")

            if (!sd.exists())
                success = sd.mkdir()

            if (success) {
                // directory exists or already created
                val dest = File(sd, "filename.txt")
                try {
                    // response is the data written to file
                    //PrintWriter(dest).use { out -> out.println("Hola mauricioo") }
                } catch (e: Exception) {
                    // handle the exception
                }
            }
        } else {
            // directory creation is not successful
        }
    }
}