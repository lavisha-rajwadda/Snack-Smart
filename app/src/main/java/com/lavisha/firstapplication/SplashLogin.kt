package com.lavisha.firstapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashLogin : AppCompatActivity() {

    private val smartTips = listOf(
        "💧 Stay hydrated with infused water!",
        "🥕 Swap chips with crunchy veggies!",
        "🍫 Dark chocolate beats candy!",
        "🍎 An apple a day keeps junk away!",
        "🥤 Ditch soda – go for smoothies!",
        "🍿 Air-popped popcorn for movie nights!",
        "🥗 Add colors to your plate!",
        "🍌 Frozen bananas = natural ice cream!",
        "📏 Smaller bowls, smarter portions!",
        "🌞 Snack Smart. Live Better."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_login)

        val tipView = findViewById<TextView>(R.id.tipTextView)
        val userNameView = findViewById<TextView>(R.id.welcomeTextView)

        val randomTip = smartTips.random()

        // ✨ Set texts
        userNameView.text = "Welcome Back!"
        tipView.text = randomTip

        // ⬛ Make views invisible initially
        userNameView.visibility = View.INVISIBLE
        tipView.visibility = View.INVISIBLE

        // 🌀 Create fade-in animation
        val fadeInAnim = AlphaAnimation(0f, 1f).apply {
            duration = 1000
            fillAfter = true
        }

        Handler(Looper.getMainLooper()).postDelayed({
            userNameView.visibility = View.VISIBLE
            userNameView.startAnimation(fadeInAnim)
        }, 300) // fade in username after 300ms

        Handler(Looper.getMainLooper()).postDelayed({
            tipView.visibility = View.VISIBLE
            tipView.startAnimation(fadeInAnim)
        }, 1000) // fade in tip after 1s

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, Welcome::class.java))
            finish()
        }, 5000) // 5 seconds total splash
    }
}
