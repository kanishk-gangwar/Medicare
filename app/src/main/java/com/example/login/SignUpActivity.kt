package com.example.login

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignUpActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var db : FirebaseFirestore
    private lateinit var reference: DatabaseReference
    private lateinit var  signUpName : EditText
    private lateinit var  signUpEmail : EditText
    private lateinit var signUpPhone : EditText
    private lateinit var signUpPassword : EditText
    private lateinit var signUpCPassword : EditText
    private lateinit var signUpPasswordLayout : TextInputLayout
    private lateinit var signUpCPasswordLayout : TextInputLayout

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        db = FirebaseFirestore.getInstance()
        signUpName = findViewById(R.id.signUpName)
        signUpEmail = findViewById(R.id.signUpEmail)
        signUpPhone = findViewById(R.id.signUpPhone)
        signUpPassword = findViewById(R.id.signUpPassword)
        signUpCPassword = findViewById(R.id.signUpCPassword)
        signUpPasswordLayout = findViewById(R.id.signUpPasswordLayout)
        signUpCPasswordLayout = findViewById(R.id.signUpCPasswordLayout)

        val signUpBtn : Button = findViewById(R.id.SignUpButton)

        val signInText : TextView = findViewById(R.id.signInText)

        signInText.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        signUpBtn.setOnClickListener {
            reference = database.getReference("person")
                 saveUsersData()

            signUpPasswordLayout.isPasswordVisibilityToggleEnabled = true
            signUpCPasswordLayout .isPasswordVisibilityToggleEnabled =true

                }

            }



    private fun saveUsersData() {
        val custompgbar = Dialog(this)
        custompgbar.setContentView(R.layout.custompgbar)
        custompgbar.setCanceledOnTouchOutside(false)
        custompgbar.show()
        val name = signUpName.text.toString()
        val email = signUpEmail.text.toString()
        val phone = signUpPhone.text.toString()
        val password = signUpPassword.text.toString()
        val Cpassword = signUpCPassword.text.toString()
        if (name.isEmpty()) {
            signUpName.error = "Enter your name"
        }
        if (email.isEmpty()) {
            signUpEmail.error = "Enter your email"
        }
        if (phone.isEmpty()) {
            signUpPhone.error = "Enter your phone number"
        } else if (phone.length != 10) {
            custompgbar.dismiss()
            signUpPhone.error = "Enter a valid Phone number"
            Toast.makeText(this, "Enter a valid Phone number", Toast.LENGTH_SHORT).show()
        }


        if (password.isEmpty()){
            signUpPassword.error = "Enter your password"
            signUpPasswordLayout.isPasswordVisibilityToggleEnabled = true
        }
        if (Cpassword.isEmpty()){
            signUpCPassword.error = "Re enter your password"
            signUpCPasswordLayout .isPasswordVisibilityToggleEnabled =true
        }

        else if(password.length < 6){
            custompgbar.dismiss()
            signUpPassword.error = "Enter password more than 6 characters"
            Toast.makeText(this,"Enter password more than 6 characters",Toast.LENGTH_SHORT).show()
        }else if(password != Cpassword){
            custompgbar.dismiss()
            signUpCPassword.error = "Password doesn't match, try again"
            Toast.makeText(this,"Password doesn't match, try again",Toast.LENGTH_SHORT).show()
        }
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener (this){
            if(it.isSuccessful){
                val currentUser=auth.currentUser!!.uid
                val userId = currentUser
                val user = Users(name, email, phone)
                reference.child(userId).setValue(user).addOnCompleteListener {
                    custompgbar.dismiss()
                    Toast.makeText(this, "Data Stored Successfully", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
        }

    }
}