package com.lavisha.firstapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class Welcome : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private var currentUserId: String? = null  // Store the user ID here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Set up the toolbar
        setSupportActionBar(findViewById(R.id.toolbar))

        // Initialize ViewPager2 and TabLayout
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        // Set adapter for ViewPager2
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Attach TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Home"
                1 -> "My Saved Snacks"
                else -> ""
            }
        }.attach()
    }

    // Create options menu for settings, help, share, rate, and logout
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_welcome, menu)
        return true
    }

    // Handle actions from the options menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_settings -> startActivity(Intent(this, Settings::class.java))
            R.id.menu_help -> startActivity(Intent(this, HelpSupport::class.java))
            R.id.menu_share -> shareApp()
            R.id.menu_rate -> rateApp()
            R.id.menu_logout -> logout()
        }
        return super.onOptionsItemSelected(item)
    }

    // Share the app using a chooser
    private fun shareApp() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, "Check out SnackSmart! Download now: [App Link]")
        startActivity(Intent.createChooser(intent, "Share via"))
    }

    // Rate the app on Google Play
    private fun rateApp() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = android.net.Uri.parse("market://details?id=com.lavisha.firstapplication")
        startActivity(intent)
    }

    // Logout the user and return to Login screen
    private fun logout() {
        val intent = Intent(this, Login::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // Add a method to navigate to Search activity with userId passed
    private fun navigateToSearch() {
        if (currentUserId != null) {
            val intent = Intent(this, Search::class.java)
            intent.putExtra("userId", currentUserId)  // Pass userId to Search activity
            startActivity(intent)
        } else {
            Toast.makeText(this, "No user ID found. Please log in again.", Toast.LENGTH_SHORT).show()
        }
    }

    // Call navigateToSearch when you need to go to Search activity (e.g., when a button is clicked)
    // You can call this method wherever appropriate based on your app's flow.
}
