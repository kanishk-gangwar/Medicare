package com.example.login

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.login.databinding.ActivityMainBinding
import com.example.login.databinding.ActivitySignInBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var signInEmail: EditText
    private lateinit var signInPassword: EditText
    private lateinit var name: TextView
    private lateinit var reference: DatabaseReference

    private lateinit var signInBtn: Button
    private lateinit var signUpText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        signInEmail = findViewById(R.id.signInEmail)
        signInPassword = findViewById(R.id.signInPassword)
        signInBtn = findViewById(R.id.SignInButton)
        signUpText = findViewById(R.id.signUpText)
        val signInPasswordLayout: TextInputLayout = findViewById(R.id.signInPasswordLayout)
        signUpText.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
            val i = Intent(this@SignInActivity, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }


        signInBtn.setOnClickListener {
            val custompgbar = Dialog(this)
            custompgbar.setContentView(R.layout.custompgbar)
            custompgbar.setCanceledOnTouchOutside(false)
            custompgbar.show()
            signInPasswordLayout.isPasswordVisibilityToggleEnabled = true
            val email = signInEmail.text.toString()
            val password = signInPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) {
                    signInEmail.error = "Enter your email address"
                }
                if (password.isEmpty()) {
                    signInPassword.error = "Enter your password"
                }
                custompgbar.dismiss()
                Toast.makeText(this, "Enter Valid details", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                signInPasswordLayout.isPasswordVisibilityToggleEnabled = false
                custompgbar.dismiss()
                signInPassword.error = "Enter password more than 6 characters"
                Toast.makeText(this, "Enter password more than 6 characters", Toast.LENGTH_SHORT)
                    .show()
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        custompgbar.dismiss()
                        Toast.makeText(this, "Successfully logged In", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Something went wrong,try again", Toast.LENGTH_SHORT)
                            .show()
                        custompgbar.dismiss()
                    }
                }
            }

        }

    }
}