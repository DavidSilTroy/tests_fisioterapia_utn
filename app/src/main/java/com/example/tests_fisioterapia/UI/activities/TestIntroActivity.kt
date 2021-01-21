package com.example.tests_fisioterapia.UI.activities

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.controllers.TestsWorking
import com.example.tests_fisioterapia.network.GetTestData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase



class TestIntroActivity: AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()    //para la base de datos
    private var user = ""
    private var idP = ""
    private var testName = ""
    private var testType = ""
    private var isFragmentLoaded = false
    lateinit var dbTest : GetTestData
    private lateinit var auth: FirebaseAuth             //para la autenticación de firebase
    val testList = TestsWorking().testsOnWork
    lateinit var dataList : MutableMap<String,String>
    val manager = supportFragmentManager
    lateinit var btn_next : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_intro)
        auth = Firebase.auth
        dataList = hashMapOf()
        idP =  intent.getStringExtra("patientId")
        testName =  intent.getStringExtra("testName")
        testType =  intent.getStringExtra("testType")
        dbTest = GetTestData(testType,testName)
    }

    override fun onStop() {
        //this.finishAfterTransition()
        super.onStop()
    }

    override fun onStart(){
        getDBdata()
        val currentUser = auth.currentUser
        val end = currentUser!!.email!!.indexOf("@")
        user = currentUser.email!!.substring(0,end)
        super.onStart()
    }

    fun showMsg(message:String){
        Toast.makeText(applicationContext,
                message
                , Toast.LENGTH_LONG).show()
    }
    fun getDBdata(){
        db.collection("tipos_de_tests")
                .document(testType)
                .collection("information")
                .document(testName)
                .get()
                .addOnCompleteListener {
                    if(it.result?.data.isNullOrEmpty()){
                        Toast.makeText(applicationContext,
                                "No hay datos"
                                , Toast.LENGTH_LONG).show()
                    }else{
                        val documents = it.result?.data!!
                        dataList["method"] = documents["method"].toString()
                        dataList["name"] = documents["name"].toString()
                        dataList["description"] = documents["description"].toString()
                        showETdata()
                    }
                }
    }
    fun showETdata(){
        val title = findViewById<TextView>(R.id.tv_title_test_intro)
        val description = findViewById<TextView>(R.id.tv_resume_test_intro)
        val method = findViewById<TextView>(R.id.tv_method_test_intro)

        title.text       = dataList["name"]
        description.text = dataList["description"]
        method.text      = dataList["method"]
    }
    fun showFragment(){
        //Para decidir el fragment que se va a enseñar
        val transaction = manager.beginTransaction()
        val fragment = TestsWorking().testFragment(testName)
        val bundle = bundleOf("patientId" to idP,"user" to user)
        fragment.arguments = bundle
        transaction.replace(R.id.fragment_test_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        isFragmentLoaded = true
        findViewById<ConstraintLayout>(R.id.cl_intro_test).visibility = View.GONE
    }

    fun btn_test_start(view: View) {
        val btn_start_test = findViewById<Button>(R.id.btn_empezar_test)
        val pb_searching = findViewById<ProgressBar>(R.id.pb_searching_intro_test)
        val tv_comment = findViewById<TextView>(R.id.tv_comment_test_intro)
        var testExist = false
        btn_start_test.visibility = View.GONE
        pb_searching.visibility = View.VISIBLE

        for (test in testList){
            if (test == testName){
                testExist = true
            }
        }
        if (testExist){
            showFragment()
            pb_searching.visibility = View.GONE
            tv_comment.visibility = View.GONE
        }else{
            Handler().postDelayed({
                showMsg("Pronto estará disponible este test!")
                btn_start_test.visibility = View.VISIBLE
                pb_searching.visibility = View.GONE
            }, 1000)
        }
    }



}