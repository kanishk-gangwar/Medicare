package com.example.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.login.adapters.MedAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Date


class MainActivity : AppCompatActivity() {


    private lateinit var database: FirebaseDatabase
    private lateinit var recyclerview : RecyclerView
    private lateinit var GreetText: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var Logoutbtn: Button
    private lateinit var name: TextView
    private lateinit var loggedEmail: String
    private lateinit var reference: DatabaseReference
    private lateinit var medArrayList: ArrayList<medicines>
    private lateinit var searchList: ArrayList<medicines>
    private lateinit var buymedicine: Button
    lateinit var msearchText : EditText
    lateinit var insertbtn : RelativeLayout
    lateinit var searchbtn : RelativeLayout

    private lateinit var adapter: MedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("person")
        Logoutbtn = findViewById(R.id.Logoutbtn)
        auth = FirebaseAuth.getInstance()
        buymedicine = findViewById(R.id.BuyMedicine)
        name = findViewById(R.id.Name)
        insertbtn = findViewById(R.id.insertdata)
        msearchText = findViewById(R.id.SearchM)
        recyclerview = findViewById(R.id.recylerview)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.setHasFixedSize(true)
        medArrayList = arrayListOf<medicines>()
        searchList = arrayListOf<medicines>()
        searchbtn = findViewById(R.id.searchbtn)


        if (auth.currentUser != null) {
            auth.currentUser!!.email.toString().also { loggedEmail = it }
            val uid = auth.currentUser!!.uid
            //    Toast.makeText(this, "$uid", Toast.LENGTH_SHORT).show()
            val name = reference.child(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    val userName = it.child("name").value
                    name.text = userName.toString()
                    // Toast.makeText(this, "$name", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        Log.d("Kanishk", auth.uid.toString() + "  " + auth.currentUser)

        Logoutbtn.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this, FrontPage::class.java)
            startActivity(intent)
        }
        searchbtn.setOnClickListener {
            fun String.removeSpace() = trim().replace(" ".toRegex(), replacement = "")
            searchByName(msearchText.text.toString().removeSpace())
        }
        buymedicine.setOnClickListener {
            val intent = Intent(this, BuyMedicines::class.java)
            startActivity(intent)
        }


        val date = Date()
        val cal: Calendar = Calendar.getInstance()
        cal.time = date
        val hour: Int = cal.get(Calendar.HOUR_OF_DAY)
        GreetText = findViewById(R.id.GreetText)
        val GreetBg= findViewById<LinearLayout>(R.id.GeetBg)


        if (hour in 0..12) {
            GreetText.text = "Good Morning!!"
            GreetBg.setBackgroundResource(R.drawable.gm)
        } else if (hour in 12..17) {
            GreetText.text = "Good Afternoon!!"
            GreetBg.setBackgroundResource(R.drawable.ga)
        } else if (hour in 17..21) {
            GreetText.text = "Good Evening!!"
            GreetBg.setBackgroundResource(R.drawable.ge)
        } else {
            GreetText.text = "Good Night!!"
            GreetBg.setBackgroundResource(R.drawable.gn)
        }
        if (auth.currentUser == null) {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        // getting data function from firebase
        getMedData()


        insertbtn.setOnClickListener {
            val intent = Intent(this, InsertMed::class.java)
            startActivity(intent)
        }

    }



    private fun getMedData() {
        val custompgbar = Dialog(this)
        custompgbar.setContentView(R.layout.custompgbar)
        custompgbar.setCanceledOnTouchOutside(false)
        custompgbar.show()
       recyclerview.visibility = android.view.View.GONE
       // progressBar.setVisibility(View.VISIBLE)
        reference = database.getReference("medicines")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                medArrayList.clear()

                if (snapshot.exists()) {
                    for (medicinesSnapshot in snapshot.children) {

                        val medicine = medicinesSnapshot.getValue(medicines::class.java)
                        medArrayList.add(medicine!!)
                    }
                    searchList.addAll(medArrayList)
                    val mAdapter = MedAdapter(medArrayList)
                    recyclerview.adapter = mAdapter
                    setupSearchEditText(mAdapter)

                    mAdapter.setOnItemClickListener(object : MedAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                           val intent = Intent(this@MainActivity, MedicineDetails::class.java)
                            //put extra
                            intent.putExtra("medname", medArrayList[position].medname)
                            intent.putExtra("description", medArrayList[position].description)
                            intent.putExtra("symptoms", medArrayList[position].symptoms)
                            startActivity(intent)
                        }

                    })
                    custompgbar.dismiss()

                //    progressBar.setVisibility(View.GONE)
                    recyclerview.visibility = android.view.View.VISIBLE
                }else{
                    custompgbar.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
                custompgbar.dismiss()
            }


        })
    }


private fun searchByName(name: String) {


    reference = database.getReference("medicines")
    // adding a value listener to database reference to perform search
    reference.addValueEventListener(object: ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            // Checking if the value exists
            if (snapshot.exists()){
                searchList.clear()
                // looping through to values
                var flag=false
                for (i in snapshot.children){
                    val med = i.getValue(medicines::class.java)
                    // checking if the name searched is available and adding to the array list
                    if (med!!.medname == name){
                        flag=true
//                        Toast.makeText(applicationContext, "$name details exists", Toast.LENGTH_LONG).show()
                        searchList.add(med)
                    }
                }
                if(flag){
                    Toast.makeText(applicationContext, "$name details exists", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@MainActivity,"this medicines does not exist, want to add? ", Toast.LENGTH_SHORT).show()
                }
//                    Toast.makeText(applicationContext, "Data does not exist", Toast.LENGTH_SHORT).show()
                //setting data to recyclerview
                searchList.addAll(medArrayList)
                recyclerview.adapter!!.notifyDataSetChanged()
            } else{
                Toast.makeText(applicationContext, "Data does not exist", Toast.LENGTH_SHORT).show()
            }

        }

        override fun onCancelled(error: DatabaseError) {
        }

    })
}

    private fun setupSearchEditText(adapter: MedAdapter) {
        val searchMed = findViewById<EditText>(R.id.SearchM)
        searchMed.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not used
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not used
            }

            override fun afterTextChanged(s: Editable?) {
                val searchEmployeeId = s.toString()
                val searchEmpName = s.toString().capitalize().trim()

                if (searchEmpName.isBlank()) {
                    // If the search text is empty, restore the original data in the RecyclerView
                    adapter.submitList(medArrayList)
                } else {
                    searchbtn.setOnClickListener {
                        // Filter the employee data based on the entered employee ID
                        val filteredList = medArrayList.filter { it.medname == searchEmpName }
                        if (filteredList.isNotEmpty()) {
                            adapter.submitList(filteredList)
                        } else {
                            Toast.makeText(this@MainActivity, "No Medicine found", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }
        })
    }

}


