package com.example.tests_fisioterapia.UI.fragments

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.UI.activities.TestResultActivity
import com.example.tests_fisioterapia.network.GetTestData
import com.example.tests_fisioterapia.network.PATTERN_DATE
import com.example.tests_fisioterapia.network.SetTestData
import java.text.SimpleDateFormat

class BurpeeFragment:Fragment(){
    lateinit var dbTest : GetTestData
    val TAG = "fragmentBurpee"
    private var result_info_extra = ""
    private var user = ""
    private var idP = ""
    private var testType = "Resistencia"
    private var testName = "Burpee"
    private val timeDB = 59
    private var timerIsRunning = false
    private lateinit var btn_burprees : ImageButton
    private lateinit var rl_loading_test : RelativeLayout
    private lateinit var tv_title : TextView
    private lateinit var tv_text : TextView
    private lateinit var tv_timer : TextView
    private lateinit var et_repetitions : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"onCreate")
        dbTest = GetTestData(testType,testName)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG,"onCreateView")
        val view: View = inflater.inflate(R.layout.fragment_burpee,container,false)
        tv_title = view.findViewById<TextView>(R.id.tv_title_burpee_fragment)
        tv_text = view.findViewById<TextView>(R.id.tv_text_burpee_fragment)
        tv_timer = view.findViewById<TextView>(R.id.tv_timer_burpee_fragment)
        btn_burprees = view.findViewById<ImageButton>(R.id.btn_burpee_fragment)
        et_repetitions = view.findViewById(R.id.et_repetition_burpee_fragment)
        SetDataInFragment()
        btn_burprees.setOnClickListener{
            if(!timerIsRunning){
                timer_in_tv()
            }else{
                if (et_repetitions.text.isNullOrEmpty()){
                    showMsg("Debe escribir el número de repeticiones")
                }else{
                    burpeTest()
                }
            }
        }
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        idP = requireArguments().getString("patientId").toString()
        user = requireArguments().getString("user").toString()
    }
    override fun onDestroy() {
        this.activity!!.finish()
        super.onDestroy()
    }
    private fun showMsg(message:String){
        Toast.makeText(this.context,
                message
                , Toast.LENGTH_LONG).show()
    }
    private fun SetDataInFragment(){
        et_repetitions.alpha = 0.4f
        et_repetitions.isEnabled = false
        btn_burprees.setBackgroundResource(R.drawable.empezar_boton_text)
        dbTest.testSteps.get().addOnCompleteListener {
            val documents = it.result?.documents!!
            if(documents.isNullOrEmpty()){
                showMsg("No se han ecnontrado datos")
            }else{
                val data = documents.get(0).data!!
                if(data.isNullOrEmpty()){
                    showMsg("Faltan datos..")
                }else{
                    tv_title.text= data.get("title").toString()
                    tv_text.text= data.get("text").toString()
                }
            }
        }
    }
    private fun timer_in_tv(){
        btn_burprees.alpha = 0.5f
        btn_burprees.isEnabled = false
        timerIsRunning = true
        var seconds = 0
        val timer = object: CountDownTimer((timeDB*1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsToShow = timeDB-seconds
                if(secondsToShow<10){
                    val to_tv = "00:0$secondsToShow"
                    tv_timer.text = to_tv
                }else{
                    val to_tv = "00:$secondsToShow"
                    tv_timer.text = to_tv
                }
                seconds+=1
            }
            override fun onFinish() {
                tv_timer.text = "00:00"
                btn_burprees.alpha = 1f
                btn_burprees.isEnabled = true
                et_repetitions.isEnabled = true
                et_repetitions.alpha = 1f
                btn_burprees.setBackgroundResource(R.drawable.finalizar_boton_text)
            }
        }
        timer.start()
    }
    private fun burpeTest(){
        btn_burprees.alpha = 0.5f
        val date = System.currentTimeMillis()
        val result_rd = et_repetitions.text.toString().toInt()
        val saveDataDB = SetTestData(testType,testName,idP,date.toString())
        val dateString: String = SimpleDateFormat(PATTERN_DATE).format(date)

        result_info_extra = "Menos de 30 repeticiones sería una marca muy negativa.\n" +
                "Entre 30 y 40 es una medida normal.\n"+
                "Entre 40 y 50 es un muy buen dato.\n"+
                "Entre 50 y 60 es una marca excelente que sólo consiguen los deportistas muy preparados.\n"

        val result_txt = when{
            result_rd<30 -> "tiene una marca negativa, menos de 30 repeticiones."
            (result_rd>=30)and(result_rd<40)-> "tiene una marca media, entre 30 y 40 repeticiones"
            (result_rd>=40)and(result_rd<50)-> "tiene una marca buena, entre 40 y 50 repeticiones."
            (result_rd>=50)and(result_rd<60)-> "tiene una marca excelente, entre 50 y 60 repeticiones"
            else -> "tiene una marca muy alta, más de 60 repeticiones"
        }
        val resultTestString = "Tu paciente hizo $result_rd repeticiones, $result_txt"
        val data = mapOf<String,Any>(
                "test_name" to testName,
                "created" to dateString,
                "created_by" to user,
                "repetitions" to result_rd,
                "result_txt" to result_txt,
                "total_result" to resultTestString
        )
        saveDataDB.toTestData.set(data).addOnSuccessListener {
            saveDataDB.relateData()
            Handler().postDelayed({
                //cambiando de activity al guardar el dato
                val intent = Intent(this.context, TestResultActivity::class.java).apply{
                    putExtra("result_test", resultTestString)
                    putExtra("test_name", testName)
                    putExtra("result_info_extra", result_info_extra)
                }
                this.onDestroy()
                startActivity(intent)
            }, 1000)

        }

    }
}