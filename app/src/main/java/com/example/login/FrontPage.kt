package com.example.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class FrontPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_front_page)


    }

    fun signup(view: View) {
        val intent = Intent(this,SignUpActivity::class.java)
        startActivity(intent)
    }
    fun sigin(view: View) {
        val intent = Intent(this,SignInActivity::class.java)
        startActivity(intent)
    }
}