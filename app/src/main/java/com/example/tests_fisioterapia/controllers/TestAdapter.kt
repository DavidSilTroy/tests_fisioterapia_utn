package com.example.tests_fisioterapia.controllers

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.startActivity
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.UI.activities.AddTestActivity
import com.example.tests_fisioterapia.UI.activities.PatientInfoActivity
import com.example.tests_fisioterapia.UI.activities.TestIntroActivity
import com.example.tests_fisioterapia.network.DeleteTestsPatientInfo


class testData(
        val name:String,
        val type:String,
        val idPatient:String,
        val pass:Boolean = false
)
class testInfoData(
    val name:String,
    val result:String,
    val created:String,
    val testId:String,
    val testPosition:String,
    val idPatient: String
)

class TestAdapter(val tests:List<testData>, val context: Context): RecyclerView.Adapter<TestAdapter.TestHolder>(){

    //cuantos elementos tenemos
    override fun getItemCount(): Int = tests.size
    //cuales son los datos que vamos a cargar
    override fun onBindViewHolder(holder: TestHolder, position: Int) {
       holder.render(tests[position])
    }
    //cual es el diseño que se va a usar para la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TestHolder(layoutInflater.inflate(R.layout.item_test_to_select , parent,false),context)
    }
    //como se enlazan los elementos visuales
    class TestHolder(val view: View, val context: Context): RecyclerView.ViewHolder(view){
        fun render(tests: testData){

            view.findViewById<TextView>(R.id.tv_test_name_item).text = tests.name
            if (tests.pass){
                view.setOnClickListener {
                    it.alpha = 0.5f
                    Handler().postDelayed({
                        it.alpha = 1f
                    }, 800)
                    val intent = Intent(context.applicationContext , TestIntroActivity::class.java).apply{
                        putExtra("testName",tests.name)
                        putExtra("testType",tests.type)
                        putExtra("patientId", tests.idPatient)
                    }
                    ContextCompat.startActivity(context, intent, Bundle())
                }
            }else{
                view.setOnClickListener {
                    it.alpha = 0.5f
                    Handler().postDelayed({
                        it.alpha = 1f
                    }, 800)
                    val intent = Intent(context.applicationContext , AddTestActivity::class.java).apply{
                        putExtra("testName",tests.name)
                        putExtra("testType",tests.name)
                        putExtra("patientId", tests.idPatient)
                    }
                    ContextCompat.startActivity(context, intent, Bundle())
                }
            }

        }
    }

}

class TestInfoAdapter(val tests:List<testInfoData>, val context: Context): RecyclerView.Adapter<TestInfoAdapter.TestInfoHolder>(){

    //cuantos elementos tenemos
    override fun getItemCount(): Int = tests.size
    //cuales son los datos que vamos a cargar
    override fun onBindViewHolder(holder: TestInfoHolder, position: Int) {
        holder.render(tests[position])
    }
    //cual es el diseño que se va a usar para la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestInfoHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TestInfoHolder(layoutInflater.inflate(R.layout.item_test_info , parent,false),context)
    }
    //como se enlazan los elementos visuales
    class TestInfoHolder(val view: View, val context: Context): RecyclerView.ViewHolder(view){
        fun render(tests: testInfoData){
            val txtTestName = view.findViewById<TextView>(R.id.tv_test_name_info_item)
            val txtTestCreated = view.findViewById<TextView>(R.id.tv_test_created_info_item)
            val txtTestResult = view.findViewById<TextView>(R.id.tv_test_result_info_item)
            txtTestName.text = tests.name
            txtTestCreated.text = tests.created
            txtTestResult.text = tests.result

            view.findViewById<ImageView>(R.id.iv_arrow_down_test_info_item).setOnClickListener{
                view.findViewById<LinearLayout>(R.id.layout_test_info_details_item).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.iv_arrow_up_test_info_item).visibility = View.VISIBLE
                it.visibility = View.GONE
            }
            view.findViewById<ImageView>(R.id.iv_arrow_up_test_info_item).setOnClickListener{
                view.findViewById<LinearLayout>(R.id.layout_test_info_details_item).visibility = View.GONE
                view.findViewById<ImageView>(R.id.iv_arrow_down_test_info_item).visibility = View.VISIBLE
                it.visibility = View.GONE
            }
            view.findViewById<ImageButton>(R.id.btn_generar_pdf_test_info_item).setOnClickListener {
                it.alpha = 0.5f
                Handler().postDelayed({
                    it.alpha = 1f
                }, 800)
                
            }


            view.findViewById<ImageButton>(R.id.btn_eliminar_test_info_item).setOnClickListener{
                it.alpha = 0.5f
                Handler().postDelayed({
                    it.alpha = 1f
                }, 800)
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Eliminar ${tests.name}")
                builder.setMessage("Confirme que desea ELIMINAR el test de ${tests.name}")
                builder.setPositiveButton("Confirmar"){ dialogInterface: DialogInterface, i: Int ->
                    val deleteTest = DeleteTestsPatientInfo(tests.idPatient,tests.testId,tests.testPosition)
                    deleteTest.deteTest()
                    view.visibility = View.GONE

                }
                builder.setNeutralButton("Cancelar") { dialogInterface: DialogInterface, i: Int ->
                }
                builder.show()
            }

        }
    }




}
