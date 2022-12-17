package com.example.login

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class BuyMedicines : AppCompatActivity() {

    private lateinit var onemg : Button
    private lateinit var pharmeasy : Button
    private lateinit var truemeds : Button
    private lateinit var netmeds : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_medicines)
        onemg = findViewById(R.id.onemg)
        pharmeasy = findViewById(R.id.PharmEasy)
        truemeds = findViewById(R.id.truemeds)
        netmeds = findViewById(R.id.netMeds)

        onemg.setOnClickListener {
            val gourl= Intent(Intent.ACTION_VIEW, Uri.parse("https://www.1mg.com/"))
            startActivity(gourl)
        }
        pharmeasy.setOnClickListener {
            val gourl= Intent(Intent.ACTION_VIEW, Uri.parse("https://pharmeasy.in/"))
            startActivity(gourl)
        }
        truemeds.setOnClickListener {
            val gourl= Intent(Intent.ACTION_VIEW, Uri.parse("https://www.truemeds.in/"))
            startActivity(gourl)
        }
        netmeds.setOnClickListener {
            val gourl= Intent(Intent.ACTION_VIEW, Uri.parse("https://www.netmeds.com/"))
            startActivity(gourl)
        }

    }
}