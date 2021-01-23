package com.example.tests_fisioterapia.UI.activities

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.capitalizeFirstLetter
import com.example.tests_fisioterapia.network.UserDBData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView
import java.lang.Exception

class AddUserDataActivity: AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    lateinit var databaseData : UserDBData
    var selectedPhtoUri: Uri? = null
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
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhtoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhtoUri)
            val BitmapDrawable = BitmapDrawable(bitmap)
            val img_patient = findViewById<ImageView>(R.id.iv_user_picture_add)
            img_patient.setImageBitmap(BitmapDrawable.toBitmap())
        }
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
        setPhotoToIV()
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

    fun setPhotoToIV(){
        val storage = Firebase.storage
        val storageRef = storage.reference
        val patientImgRef = storageRef.child("images/$user")
        val imageView = findViewById<CircleImageView>(R.id.iv_user_picture_add)
        val MAX_BYTES: Long = 10485760 //10Mb
        val progresbarPhoto = findViewById<ProgressBar>(R.id.pb_user_picture_add)

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
            }
        } catch (e: Exception) {
            progresbarPhoto.visibility = View.GONE
        }


    }
    fun uploadPatienImage(){
        Toast.makeText(applicationContext, "Revisando los datos.." , Toast.LENGTH_LONG).show()
        val filename = user
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhtoUri!!).addOnCompleteListener{
            saveData()
        }
    }



    fun btn_save_user_data(view: View){
        view.visibility = View.INVISIBLE
        if(getETdata()){
            saveData()
            uploadPatienImage()
        }else{
            view.visibility = View.VISIBLE
        }
    }

    fun btn_editUserPhoto(view: View) {
        view.alpha = 0.5f
        Handler().postDelayed({
            view.alpha = 1f
        }, 800)

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)

    }
}