package com.lavisha.firstapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SavedSnacksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_snacks)

        // Load the fragment into the container
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SavedSnacksFragment())
            .commit()
    }
}
