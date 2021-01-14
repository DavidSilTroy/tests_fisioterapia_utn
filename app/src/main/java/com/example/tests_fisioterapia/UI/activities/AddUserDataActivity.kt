package com.example.tests_fisioterapia.UI.activities

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.capitalizeFirstLetter
import com.example.tests_fisioterapia.network.UserDBData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AddUserDataActivity: AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    lateinit var databaseData : UserDBData
    private var email = ""
    private var user = ""
    private var name = ""
    private var last_name = ""
    private var semester = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user_data)
        auth = Firebase.auth
        email = auth.currentUser!!.email.toString()
        val end = email.indexOf("@")
        user = email.substring(0,end)
        databaseData = UserDBData(user)
        putETdata()
    }

    fun getETdata():Boolean{
        name = findViewById<EditText>(R.id.et_name_user).text.toString().capitalizeFirstLetter()
        last_name = findViewById<EditText>(R.id.et_last_name_user).text.toString().capitalizeFirstLetter()
        semester = findViewById<EditText>(R.id.et_user_semester).text.toString().capitalizeFirstLetter()
        var msj = ""
        msj = when{
            name.isBlank() -> "Debe escribir su nombre"
            last_name.isBlank() -> "Debe escribir su apellido"
            semester.isBlank() -> "Debe escribir el semestre en el que está"
            else->{
                return true
            }
        }
        Toast.makeText(applicationContext,msj,Toast.LENGTH_LONG).show()
        return false
    }
    fun putETdata(){
        findViewById<TextView>(R.id.et_user_user).text = user
        findViewById<TextView>(R.id.et_user_email).text = email
        val nameET = findViewById<TextView>(R.id.et_name_user)
        val last_nameET = findViewById<TextView>(R.id.et_last_name_user)
        val semesterET = findViewById<TextView>(R.id.et_user_semester)
        databaseData.userData.get().addOnCompleteListener{
            if(it.result?.data.isNullOrEmpty()){
                databaseData.saveUserAndEmail(user,email)
                nameET.text = ""
                last_nameET.text = ""
                semesterET.text = ""
            }else{
                val nameDB = it.result?.data!!.get("name").toString()
                val last_nameDB =it.result?.data!!.get("last_name").toString()
                val semesterDB = it.result?.data!!.get("semester").toString()
                if(nameDB == "null"){nameET.text = ""}else{ nameET.text=nameDB}
                if(last_nameDB == "null"){last_nameET.text = ""}else{last_nameET.text=last_nameDB}
                if(semesterDB == "null"){semesterET.text = ""}else{semesterET.text=semesterDB}
            }
        }

    }
    fun saveData(){
        val data = hashMapOf<String,Any>(
                "semester" to semester,
                "name" to name,
                "last_name" to last_name
        )
        databaseData.saveData(data,this.finishAfterTransition())
    }

    fun btn_save_user_data(view: View){
        view.visibility = View.INVISIBLE
        if(getETdata()){
            saveData()
        }else{
            view.visibility = View.VISIBLE
        }
    }

    fun btn_editUserPhoto(view: View) {
        Toast.makeText(applicationContext, "Esta opción estará lista en una próxima versión" , Toast.LENGTH_LONG).show()
    }
}