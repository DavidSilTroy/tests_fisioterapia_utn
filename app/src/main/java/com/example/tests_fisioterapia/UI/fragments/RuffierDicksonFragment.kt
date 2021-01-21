package com.example.tests_fisioterapia.UI.fragments

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.UI.activities.AddPatientActivity
import com.example.tests_fisioterapia.UI.activities.MainActivity
import com.example.tests_fisioterapia.UI.activities.TestIntroActivity
import com.example.tests_fisioterapia.UI.activities.TestResultActivity
import com.example.tests_fisioterapia.network.GetTestData
import com.example.tests_fisioterapia.network.PATTERN_DATE
import com.example.tests_fisioterapia.network.SetTestData
import java.text.SimpleDateFormat

class RuffierDicksonFragment: Fragment(){
    lateinit var dbTest : GetTestData
    val TAG = "fragmentRuffierDickson"
    private lateinit var btn_ruffier_d : ImageButton
    private lateinit var rl_loading_test : RelativeLayout
    private lateinit var tv_title : TextView
    private lateinit var tv_text : TextView
    private lateinit var tv_timer :TextView
    private lateinit var et_pulse :EditText
    private lateinit var iv_ruffierD :ImageView
    private var result_info_extra = ""
    private var user = ""
    private var idP = ""
    private var timerIsReady = false
    private var timerIsRunning = false
    private var pulses = mutableListOf(0,0,0)
    private  var timeDB = 0
    private var totalSteps = 10
    private var currentStep = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG,"onCreate")
        /*if(isChanged) {
            val intent = Intent(this.context, IndexActivity::class.java)
            startActivity(intent)
        }*/
        dbTest = GetTestData("Cardio","Ruffier Dickson")
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG,"onCreateView")
        val view:View = inflater.inflate(R.layout.fragment_ruffier_dickson,container,false)

        tv_title = view.findViewById<TextView>(R.id.tv_title_ruffier_dickson_fragment)
        tv_text = view.findViewById<TextView>(R.id.tv_text_ruffier_dickson_fragment)
        tv_timer = view.findViewById<TextView>(R.id.tv_timer_ruffier_dickson_fragment)
        btn_ruffier_d = view.findViewById<ImageButton>(R.id.btn_ruffier_dickson)
        rl_loading_test = view.findViewById<RelativeLayout>(R.id.rl_loading_ruffier_dickson)
        et_pulse = view.findViewById<EditText>(R.id.et_pulse_ruffier_dickson_fragment)
        iv_ruffierD = view.findViewById<ImageView>(R.id.iv_ruffier_dickson_fragment)
        ruffier_dickson()
        btn_ruffier_d.setOnClickListener{
            when{
                currentStep.equals(0) or
                timerIsReady -> ruffier_dickson()
                et_pulse.text.isNullOrEmpty() -> showMsg("Debe escribir el pulso")
                else->{
                    pulses[currentStep-1] = et_pulse.text.toString().toInt()
                    et_pulse.text.clear()
                    ruffier_dickson()
                }
            }

        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //todo: borrar ese comentario al acabar
        /***Aqui debemos obtener los datos del activity padre que nos envió xddd***/
        idP = requireArguments().getString("patientId").toString()
        user = requireArguments().getString("user").toString()
    }

    override fun onDestroy() {
        this.activity!!.finish()
        super.onDestroy()
    }
    fun showMsg(message:String){
        Toast.makeText(this.context,
                message
                , Toast.LENGTH_LONG).show()
    }

    fun timer_in_tv(){
        timerIsRunning = true
        var seconds = 0
        val timer = object: CountDownTimer((timeDB*1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsToShow = timeDB-seconds
                if(timeDB-seconds<10){
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
                timerIsRunning = false
                timerIsReady = false
                btn_ruffier_d.alpha = 1f
                et_pulse.isEnabled = true
                et_pulse.alpha = 1f
            }
        }
        timer.start()




    }
    fun checkImage(){
        when(currentStep){
            1-> iv_ruffierD.setBackgroundResource(R.drawable.ruffier_dickson_img_1)
            2-> iv_ruffierD.setBackgroundResource(R.drawable.ruffier_dickson_img)
        }
    }

    fun ruffier_dickson(){
        if (!timerIsRunning){
            if(timerIsReady){
                timer_in_tv()
                btn_ruffier_d.alpha = 0.6f
                if(totalSteps == currentStep){
                    btn_ruffier_d.setBackgroundResource(R.drawable.finalizar_boton_text)
                }else{btn_ruffier_d.setBackgroundResource(R.drawable.boton_siguiente_text)}
            }else{
                rl_loading_test.visibility = View.VISIBLE
                if(totalSteps <= currentStep){
                    showMsg("Ahora deberías ir a los resultados ${pulses[0]} ${pulses[1]} ${pulses[2]}")
                    val date = System.currentTimeMillis()
                    val result_rd = ((pulses[0]+pulses[1]+pulses[2]-200).toDouble())/10
                    val saveDataDB = SetTestData("Cardio","Ruffier Dickson",idP,date.toString())
                    val dateString: String = SimpleDateFormat(PATTERN_DATE).format(date)

                    result_info_extra = "RESULTADO 0: Corazón de atleta.\n" +
                            "RESULTADO 0,1 a 5: Corazón suficientemente apto para actividades físicas.\n"+
                            "RESULTADO 5,1 a 10: Corazón medio, debería realizar un plan para recuperar el estado físico adecuado.\n"+
                            "RESULTADO 10,1 a 15: Corazón medio, hacer una visita a un profesional de la salud.\n"+
                            "RESULTADO más de 15: es un índice que revela mal estado o un corazón débil\n"
                    val result_txt = when{
                        result_rd<0.1 -> "Corazon de atleta."
                        (result_rd>=0.1)and(result_rd<5.0)-> "Corazón suficientemente apto para actividades físicas."
                        (result_rd>=5.0)and(result_rd<10.0)-> "Corazón medio, debería realizar un plan para recuperar el estado físico adecuado."
                        (result_rd>=10.0)and(result_rd<15.00)-> "Corazón medio, debería hacer una visita a un profesional de la salud."
                        else -> "Un índice que revela mal estado o un corazón débil."
                    }
                    val resultTestString = "RD es $result_rd, paciente con $result_txt"
                    val data = mapOf<String,Any>(
                        "test_name" to "Ruffier Dickson",
                        "created" to dateString,
                        "created_by" to user,
                        "P0" to pulses[0],
                        "P1" to pulses[1],
                        "P2" to pulses[2],
                        "RD" to result_rd,
                        "result_txt" to result_txt,
                        "total_result" to resultTestString
                    )
                    saveDataDB.toTestData.set(data).addOnSuccessListener {
                        saveDataDB.relateData()
                        Handler().postDelayed({
                            //cambiando de activity al guardar el dato
                            val intent = Intent(this.context, TestResultActivity::class.java).apply{
                                putExtra("result_test", resultTestString)
                                putExtra("test_name", "Test de Ruffier Dickson")
                                putExtra("result_info_extra", result_info_extra)
                            }
                            this.onDestroy()
                            startActivity(intent)
                        }, 1000)

                    }
                    //todo: guardar resultados del paciente y mostrarlos en la siguiente activity

                }else{
                    dbTest.testSteps.get().addOnCompleteListener {
                        val documents = it.result?.documents!!
                        if(documents.isNullOrEmpty()){
                            showMsg("No se han ecnontrado datos")
                        }else{
                            val data = documents.get(currentStep).data!!
                            if(data.isNullOrEmpty()){
                                showMsg("Faltan datos..")
                            }else{
                                totalSteps = documents.size
                                tv_title.text= data.get("title").toString()
                                tv_text.text= data.get("text").toString()
                                if (data.get("time").toString() != "null"){
                                    timeDB =data.get("time").toString().toInt()
                                    btn_ruffier_d.setBackgroundResource(R.drawable.empezar_boton_text)
                                    et_pulse.alpha = 0.6f
                                    et_pulse.isEnabled = false
                                    tv_timer.visibility = View.VISIBLE
                                    tv_timer.text = "00:$timeDB"
                                    timerIsReady = true
                                }
                                rl_loading_test.visibility = View.GONE
                                checkImage()
                                currentStep+=1
                            }
                        }
                    }
                }
            }
        }else{
            showMsg("Espere que el tiempo termine por favor")
            btn_ruffier_d.alpha = 0.5f
        }

    }




}

