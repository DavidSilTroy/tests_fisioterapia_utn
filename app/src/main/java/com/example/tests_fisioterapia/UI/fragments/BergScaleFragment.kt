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

class BergScaleFragment : Fragment(){
    lateinit var dbTest : GetTestData
    val TAG = "fragmentBurpee"
    private var result_info_extra = ""
    private var user = ""
    private var idP = ""
    private var testType = "Equilibrio"
    private var testName = "Escala de Berg"
    private var currentStep = 0
    private var totalSteps = 14
    private var pointsToAdd = 0
    private var totalPoints = 0
    private var isOptionSelected = false
    private lateinit var btn_berg_scale : ImageButton
    private lateinit var rl_loading_test : RelativeLayout
    private lateinit var tv_title : TextView
    private lateinit var tv_text : TextView
    private lateinit var tv_text_2 : TextView
    private lateinit var tv_option_1 : TextView
    private lateinit var tv_option_2 : TextView
    private lateinit var tv_option_3 : TextView
    private lateinit var tv_option_4 : TextView
    private lateinit var tv_option_5 : TextView
    private lateinit var img_option_1 : ImageView
    private lateinit var img_option_2 : ImageView
    private lateinit var img_option_3 : ImageView
    private lateinit var img_option_4 : ImageView
    private lateinit var img_option_5 : ImageView
    private lateinit var ll_option_1 : LinearLayout
    private lateinit var ll_option_2 : LinearLayout
    private lateinit var ll_option_3 : LinearLayout
    private lateinit var ll_option_4 : LinearLayout
    private lateinit var ll_option_5 : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"onCreate")
        dbTest = GetTestData(testType,testName)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d(TAG,"onCreateView")
        val view: View = inflater.inflate(R.layout.fragment_berg_scale,container,false)

        tv_title = view.findViewById<TextView>(R.id.tv_title_berg_scale_fragment)
        tv_text = view.findViewById<TextView>(R.id.tv_text_berg_scale_fragment)
        tv_text_2 = view.findViewById<TextView>(R.id.tv_text_2_berg_scale_fragment)
        btn_berg_scale = view.findViewById<ImageButton>(R.id.btn_berg_scale_fragment)
        rl_loading_test = view.findViewById(R.id.rl_loading_berg_scale_fragment)

        ll_option_1 = view.findViewById(R.id.ll_option_1_berg_scale_fragment)
        ll_option_2 = view.findViewById(R.id.ll_option_2_berg_scale_fragment)
        ll_option_3 = view.findViewById(R.id.ll_option_3_berg_scale_fragment)
        ll_option_4 = view.findViewById(R.id.ll_option_4_berg_scale_fragment)
        ll_option_5 = view.findViewById(R.id.ll_option_5_berg_scale_fragment)
        img_option_1 = view.findViewById(R.id.iv_option_1_berg_scale_fragment)
        img_option_2 = view.findViewById(R.id.iv_option_2_berg_scale_fragment)
        img_option_3 = view.findViewById(R.id.iv_option_3_berg_scale_fragment)
        img_option_4 = view.findViewById(R.id.iv_option_4_berg_scale_fragment)
        img_option_5 = view.findViewById(R.id.iv_option_5_berg_scale_fragment)
        tv_option_1 = view.findViewById(R.id.tv_option_1_berg_sacale_fragment)
        tv_option_2 = view.findViewById(R.id.tv_option_2_berg_sacale_fragment)
        tv_option_3 = view.findViewById(R.id.tv_option_3_berg_sacale_fragment)
        tv_option_4 = view.findViewById(R.id.tv_option_4_berg_sacale_fragment)
        tv_option_5 = view.findViewById(R.id.tv_option_5_berg_sacale_fragment)

        SetDataInFragment()
        btn_berg_scale.setOnClickListener{
            if (isOptionSelected){
                btn_berg_scale.alpha  = 0.5f
                if(currentStep==totalSteps){
                    BergScale()
                }else{
                    AddPoints()
                }
            }else{
                showMsg("Debe seleccionar una opción para continuar")
            }
        }
        ll_option_1.setOnClickListener {
            SelectOption(1)
        }
        ll_option_2.setOnClickListener {
            SelectOption(2)
            pointsToAdd = 1
        }
        ll_option_3.setOnClickListener {
            SelectOption(3)
            pointsToAdd = 2
        }
        ll_option_4.setOnClickListener {
            SelectOption(4)
            pointsToAdd = 3
        }
        ll_option_5.setOnClickListener {
            SelectOption(5)
            pointsToAdd = 4
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

    private fun SelectOption(number:Int){
        img_option_1.setBackgroundResource(R.drawable.unselected_circle_img)
        img_option_2.setBackgroundResource(R.drawable.unselected_circle_img)
        img_option_3.setBackgroundResource(R.drawable.unselected_circle_img)
        img_option_4.setBackgroundResource(R.drawable.unselected_circle_img)
        img_option_5.setBackgroundResource(R.drawable.unselected_circle_img)
        isOptionSelected = true
        when(number){
            1-> img_option_1.setBackgroundResource(R.drawable.selected_circle_img)
            2-> img_option_2.setBackgroundResource(R.drawable.selected_circle_img)
            3-> img_option_3.setBackgroundResource(R.drawable.selected_circle_img)
            4-> img_option_4.setBackgroundResource(R.drawable.selected_circle_img)
            5-> img_option_5.setBackgroundResource(R.drawable.selected_circle_img)
            else-> isOptionSelected = false
        }

    }
    private fun AddPoints(){
        totalPoints+=pointsToAdd
        SetDataInFragment()
    }
    private fun SetDataInFragment(){
        dbTest.testSteps.get().addOnCompleteListener {

            if(it.result?.documents.isNullOrEmpty()){
                showMsg("No se han ecnontrado datos")
            }else{
                val documents = it.result?.documents!!
                totalSteps=documents.size
                dbTest.testSteps.document(currentStep.toString()).get()
                        .addOnCompleteListener {
                            if(it.result?.data.isNullOrEmpty()){
                                showMsg("Faltan datos...")
                            }
                            else{
                                val data = it.result?.data!!
                                rl_loading_test.visibility = View.GONE
                                tv_title.text= data.get("title").toString()
                                tv_text.text= data.get("text_0").toString()
                                tv_text_2.text= data.get("text_1").toString()
                                tv_option_1.text= data.get("point_1").toString()
                                tv_option_2.text= data.get("point_2").toString()
                                tv_option_3.text= data.get("point_3").toString()
                                tv_option_4.text= data.get("point_4").toString()
                                tv_option_5.text= data.get("point_5").toString()
                                SelectOption(0)
                                if(currentStep == totalSteps-1){
                                    btn_berg_scale.setBackgroundResource(R.drawable.finalizar_boton_text)
                                }
                                btn_berg_scale.alpha  = 1f
                                currentStep+=1
                            }
                }
            }
        }
    }
    private fun BergScale(){
        val date = System.currentTimeMillis()
        val result_rd = totalPoints
        val saveDataDB = SetTestData(testType,testName,idP,date.toString())
        val dateString: String = SimpleDateFormat(PATTERN_DATE).format(date)

        result_info_extra = "Una puntuacion por debajo de 45 indica que existe riesgo de caida. \n" +
                "Por debajo de 40 puntos los pacientes pueden sufrir múltiples caidas.\n"

        val result_txt = when{
            result_rd<40 -> "tiene riesgo de multiples caídas"
            (result_rd>=40)and(result_rd<45)-> "tiene riesgo de caída"
            else -> "tiene un buen equilibrio"
        }
        val resultTestString = "Tu paciente tiene $result_rd puntos, $result_txt"
        val data = mapOf<String,Any>(
                "test_name" to testName,
                "created" to dateString,
                "created_by" to user,
                "points" to result_rd,
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