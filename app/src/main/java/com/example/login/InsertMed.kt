package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class InsertMed : AppCompatActivity() {
    private lateinit var etmedname : EditText
    private lateinit var etsymptoms : EditText
    private lateinit var etdescription : EditText
    private lateinit var savedatabtn : Button

    private lateinit var dbref : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_med)

        etmedname = findViewById(R.id.Insertname)
        etsymptoms = findViewById(R.id.Insertsymtoms)
        etdescription = findViewById(R.id.Insertdescription)
        savedatabtn = findViewById(R.id.insertmed)
        dbref = FirebaseDatabase.getInstance().getReference("medicines")

        savedatabtn.setOnClickListener {
            saveMeddata()
        }
    }

    private fun saveMeddata() {
        val mediname = etmedname.text.toString()
        val medisymptoms = etsymptoms.text.toString()
        val medidescription = etdescription.text.toString()

        if(mediname.isEmpty()){
            etmedname.error = "Enter the Medicine name"
        }
        if(medisymptoms.isEmpty()){
            etsymptoms.error = "Enter the Symptoms"
        }
        if(medidescription.isEmpty()){
            etdescription.error = "Enter the Description"
        }

        val medicines = medicines(mediname,medidescription,medisymptoms)

        dbref.child(mediname).setValue(medicines)
            .addOnCompleteListener {
                Toast.makeText(this,"Data Inserted Successfully",Toast.LENGTH_SHORT).show()
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener { err ->
                Toast.makeText(this,"Error ${err.message}",Toast.LENGTH_SHORT).show()

            }
    }
}