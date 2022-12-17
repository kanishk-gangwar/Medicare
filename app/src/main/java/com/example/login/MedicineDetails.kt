package com.example.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MedicineDetails : AppCompatActivity() {

    private lateinit var tvmedname : TextView
    private lateinit var tvsymptoms : TextView
    private lateinit var tvdescription : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_details)

        tvmedname = findViewById(R.id.tvmedname)
        tvsymptoms = findViewById(R.id.tvsymptoms)
        tvdescription = findViewById(R.id.tvdescription)
        setValues()
    }



    private fun setValues(){

        tvmedname.text = intent.getStringExtra("medname")
        tvsymptoms.text = intent.getStringExtra("symptoms")
        tvdescription.text = intent.getStringExtra("description")
    }
}