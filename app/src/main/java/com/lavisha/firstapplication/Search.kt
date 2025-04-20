package com.lavisha.firstapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.lavisha.firstapplication.databinding.ActivitySearchBinding

class Search : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: SubstituteAdapter
    private val firestore = FirebaseFirestore.getInstance()
    private var currentUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        currentUserId = sharedPref.getString("userId", null)

        if (currentUserId.isNullOrEmpty()) {
            Toast.makeText(this, "Please sign in to access the app.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupRecyclerView()
        setupSearch()
    }

    private fun setupRecyclerView() {
        adapter = SubstituteAdapter(
            onSaveClick = { substitute -> toggleSaveSubstitute(substitute) },
            onItemClick = { substitute -> showSubstituteDetail(substitute) }
        )

        binding.rvSubstitutes.apply {
            layoutManager = LinearLayoutManager(this@Search)
            adapter = this@Search.adapter
        }
    }

    private fun setupSearch() {
        binding.etSearchFood.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else false
        }

        binding.btnSearch.setOnClickListener {
            performSearch()
        }
    }

    private fun performSearch() {
        val query = binding.etSearchFood.text.toString().trim()
        if (query.isNotEmpty()) {
            hideKeyboard()
            searchFoodAlternatives(query)
        } else {
            Toast.makeText(this, "Please enter a food item", Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchFoodAlternatives(query: String) {
        binding.progressBar.visibility = View.VISIBLE

        firestore.collection("food_items")
            .whereEqualTo("alternative", query.lowercase())
            .get()
            .addOnSuccessListener { documents ->
                binding.progressBar.visibility = View.GONE

                if (documents.isEmpty) {
                    showEmptyState("This '$query' is under progress.")
                } else {
                    val substitutes = documents.map { doc -> Substitute.fromDocument(doc) }
                    Substitute.checkSavedStatus(
                        userId = currentUserId ?: "",
                        substitutes = substitutes,
                        onComplete = { updatedList -> showResults(updatedList) },
                        onError = {
                            Toast.makeText(this, "Failed to fetch saved status", Toast.LENGTH_SHORT).show()
                            showResults(substitutes)
                        }
                    )
                }
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                showEmptyState("Failed to fetch data. Please check your connection.")
            }
    }

    private fun toggleSaveSubstitute(substitute: Substitute) {
        currentUserId?.let { userId ->
            Substitute.toggleSave(
                userId = userId,
                substitute = substitute,
                onSuccess = { newSavedState ->
                    adapter.updateItemSavedState(substitute.id, newSavedState)
                    val message = if (newSavedState) "Saved to your collection!" else "Removed from saved items"
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                },
                onError = {
                    Toast.makeText(this, "Failed to update: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            )
        } ?: Toast.makeText(this, "Please sign in to save items", Toast.LENGTH_SHORT).show()
    }

    private fun showResults(substitutes: List<Substitute>) {
        if (substitutes.isEmpty()) {
            showEmptyState("No results found.")
            return
        }

        binding.tvResultsTitle.visibility = View.VISIBLE
        binding.rvSubstitutes.visibility = View.VISIBLE
        adapter.submitList(substitutes)
    }

    private fun showEmptyState(message: String) {
        binding.tvResultsTitle.visibility = View.GONE
        binding.rvSubstitutes.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun hideKeyboard() {
        currentFocus?.let {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    private fun showSubstituteDetail(substitute: Substitute) {
        Toast.makeText(this, "Showing details for ${substitute.name}", Toast.LENGTH_SHORT).show()
    }
}
