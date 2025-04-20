package com.lavisha.firstapplication

import android.content.Intent
import android.os.*
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        val nameText = findViewById<TextView>(R.id.textView)
        val name = " SNACK SMART"
        val handler = Handler(Looper.getMainLooper())

        // Animate app name letter-by-letter
        for (i in name.indices) {
            handler.postDelayed({
                nameText.text = name.substring(0, i + 1)
                nameText.visibility = TextView.VISIBLE
            }, i * 500L)
        }

        // Move to MainActivity after the animation
        handler.postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, name.length * 500L + 1000L)
    }
}
