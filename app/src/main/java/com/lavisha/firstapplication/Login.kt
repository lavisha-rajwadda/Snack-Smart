package com.lavisha.firstapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Login : AppCompatActivity() {

    private lateinit var finalLoginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        finalLoginButton = findViewById(R.id.loginButton)
        signUpButton = findViewById(R.id.sign_up)
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)

        firestore = FirebaseFirestore.getInstance()

        finalLoginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
            } else {
                firestore.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            val document = querySnapshot.documents[0]
                            val storedPassword = document.getString("password")
                            val userId = document.id

                            if (password == storedPassword) {
                                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                                // ✅ Save userId and username in SharedPreferences
                                val sharedPref =
                                    getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                                with(sharedPref.edit()) {
                                    putString("userId", userId)
                                    putString("username", username)
                                    apply()
                                }


                                // ✅ Navigate to Welcome screen
                                val intent = Intent(this, Welcome::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Invalid Password!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "User does not exist!", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }
    }
}
