package com.example.tests_fisioterapia.controllers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.tests_fisioterapia.R
import com.example.tests_fisioterapia.UI.activities.AddTestActivity
import com.example.tests_fisioterapia.UI.activities.TestIntroActivity


class testData(
        val name:String,
        val type:String,
        val idPatient:String,
        val pass:Boolean = false
)
class testInfoData(
    val name:String,
    val type:String
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
                    val intent = Intent(context.applicationContext , TestIntroActivity::class.java).apply{
                        putExtra("testName",tests.name)
                        putExtra("testType",tests.type)
                        putExtra("patientId", tests.idPatient)
                    }
                    ContextCompat.startActivity(context, intent, Bundle())
                }
            }else{
                view.setOnClickListener {
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
            view.findViewById<TextView>(R.id.tv_test_name_info_item).text = tests.name

        }
    }

}
