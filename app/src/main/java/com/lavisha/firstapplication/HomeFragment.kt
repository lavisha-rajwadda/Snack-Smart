package com.lavisha.firstapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private lateinit var tvWelcomeMessage: TextView
    private lateinit var tvDailyTip: TextView
    private lateinit var ivSnackImage: ImageView
    private lateinit var tvSnackName: TextView
    private lateinit var tvSnackDetails: TextView
    private lateinit var btnSearchSnacks: Button
    private lateinit var btnSavedSnacks: Button
    private lateinit var tvStreakInfo: TextView

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Bind views
        tvWelcomeMessage = view.findViewById(R.id.tvWelcomeMessage)
        tvDailyTip = view.findViewById(R.id.tvDailyTip)
        ivSnackImage = view.findViewById(R.id.ivSnackImage)
        tvSnackName = view.findViewById(R.id.tvSnackName)
        tvSnackDetails = view.findViewById(R.id.tvSnackDetails)
        btnSearchSnacks = view.findViewById(R.id.btnSearchSnacks)
        btnSavedSnacks = view.findViewById(R.id.btnSavedSnacks)
        tvStreakInfo = view.findViewById(R.id.tvStreakInfo)

        // 🔑 Get the username from SharedPreferences
        val username = getUsernameFromSharedPrefs()
        Log.d("HomeFragment", "Fetched username from SharedPrefs: $username")

        // 🔍 Query Firestore using whereEqualTo
        db.collection("users")
            .whereEqualTo("username", username)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val firstName = document.getString("firstName") ?: "User"
                    tvWelcomeMessage.text = "👋 Hello, $firstName!"
                    Log.d("HomeFragment", "Fetched firstName: $firstName")
                } else {
                    tvWelcomeMessage.text = "👋 Hello!"
                    Log.d("HomeFragment", "No matching document found for username: $username")
                }
            }
            .addOnFailureListener { exception ->
                tvWelcomeMessage.text = "👋 Hello!"
                Toast.makeText(requireContext(), "Error fetching user", Toast.LENGTH_SHORT).show()
                Log.e("HomeFragment", "Firestore error: ", exception)
            }

        // Static data
        tvDailyTip.text = "💡 Tip: Swap chips for roasted makhana!"
        tvSnackName.text = "Greek Yogurt + Berries"
        tvSnackDetails.text = "🥗 120 Cal | High Protein"
        tvStreakInfo.text = "Healthy Streak: 4 Days"
        ivSnackImage.setImageResource(R.drawable.ic_snack_yogurt)

        // Buttons
        btnSearchSnacks.setOnClickListener {
            startActivity(Intent(requireContext(), Search::class.java))
        }
        btnSavedSnacks.setOnClickListener {
            // Optional toast for user feedback
            Toast.makeText(requireContext(), "Go to Saved Snacks", Toast.LENGTH_SHORT).show()

            // Start the activity that hosts the SavedSnacksFragment
            val intent = Intent(requireContext(), SavedSnacksActivity::class.java)
            startActivity(intent)
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainer, SavedSnacksFragment()).commit()
        }
        return view
    }

    private fun getUsernameFromSharedPrefs(): String {
        val sharedPref = requireActivity().getSharedPreferences("UserPrefs", 0)
        return sharedPref.getString("username", "") ?: ""
    }
}
