package com.lavisha.firstapplication

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Registration : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val etFirstName = findViewById<EditText>(R.id.editTextFirstName)
        val etLastName = findViewById<EditText>(R.id.editTextLastName)
        val etEmail = findViewById<EditText>(R.id.editTextEmailAddress)
        val etUsername = findViewById<EditText>(R.id.editTextUsername)
        val etPassword = findViewById<EditText>(R.id.editTextPassword)
        val etDay = findViewById<EditText>(R.id.editTextDay)
        val etMonth = findViewById<EditText>(R.id.editTextMonth)
        val etYear = findViewById<EditText>(R.id.editTextYear)
        val genderGroup = findViewById<RadioGroup>(R.id.radioGroupGender)

        val btnSubmit = findViewById<Button>(R.id.buttonSubmit)
        val btnLogin = findViewById<Button>(R.id.buttonLogin)

        firestore = FirebaseFirestore.getInstance()

        btnLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        btnSubmit.setOnClickListener {
            val firstName = etFirstName.text.toString().trim()
            val lastName = etLastName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val day = etDay.text.toString().trim()
            val month = etMonth.text.toString().trim()
            val year = etYear.text.toString().trim()

            val selectedGenderId = genderGroup.checkedRadioButtonId
            val gender = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else ""

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                username.isEmpty() || password.isEmpty() ||
                day.isEmpty() || month.isEmpty() || year.isEmpty() || gender.isEmpty()
            ) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            } else {
                val user = hashMapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "email" to email,
                    "username" to username,
                    "password" to password,
                    "dob" to "$day-$month-$year",
                    "gender" to gender
                )

                firestore.collection("users")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        val userId = documentReference.id

                        // ✅ Save userId to SharedPreferences
                        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                        sharedPref.edit().putString("userId", userId).apply()
                        android.util.Log.d("Registration", "Saved userId: $userId")

                        Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
