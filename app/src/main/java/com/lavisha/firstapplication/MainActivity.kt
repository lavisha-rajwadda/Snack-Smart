package com.lavisha.firstapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {

    private lateinit var SignIn: Button
    private lateinit var SignUp: Button
    private lateinit var ContinueGuest: Button
    private lateinit var lottieAnimation: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Initialize buttons
        SignIn = findViewById(R.id.sign_in)
        SignUp = findViewById(R.id.sign_up)
        ContinueGuest = findViewById(R.id.continue_guest)
        lottieAnimation = findViewById(R.id.lottieAnimation)

        // Handle Sign In button click
        SignIn.setOnClickListener {
            Toast.makeText(this, "Sign in Clicked!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        // Handle Sign Up button click
        SignUp.setOnClickListener {
            Toast.makeText(this, "Sign up Clicked!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Registration::class.java)
            startActivity(intent)
        }

        // Handle Continue as Guest button click
        ContinueGuest.setOnClickListener {
            Toast.makeText(this, "Continuing as Guest...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, Welcome::class.java)  // Redirect to home screen or main content
            startActivity(intent)
        }

        // Optional: Control Lottie animation (if needed)
        lottieAnimation.setOnClickListener {
            if (lottieAnimation.isAnimating) {
                lottieAnimation.pauseAnimation()
                Toast.makeText(this, "Animation Paused", Toast.LENGTH_SHORT).show()
            } else {
                lottieAnimation.playAnimation()
                Toast.makeText(this, "Animation Playing", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle insets for proper UI layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
