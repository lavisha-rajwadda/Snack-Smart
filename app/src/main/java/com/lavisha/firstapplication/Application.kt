package com.lavisha.firstapplication

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Setup Firestore settings once here
        val firestore = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)  // This enables offline caching
            .build()
        firestore.firestoreSettings = settings
    }
}
