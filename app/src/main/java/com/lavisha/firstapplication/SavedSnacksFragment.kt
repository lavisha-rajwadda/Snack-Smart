package com.lavisha.firstapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.lavisha.firstapplication.databinding.FragmentSavedSnacksBinding

class SavedSnacksFragment : Fragment() {

    private var _binding: FragmentSavedSnacksBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SavedSnacksAdapter
    private lateinit var userId: String
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedSnacksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ Changed from "UserData" to "UserPrefs"
        val sharedPrefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        userId = sharedPrefs.getString("userId", "")?.trim() ?: ""

        Log.d("SavedSnacks", "✅ Loaded userId from SharedPreferences: [$userId]")
        Toast.makeText(requireContext(), "Using userId: $userId", Toast.LENGTH_LONG).show()

        setupRecyclerView()
        fetchSavedSnacks()
    }

    private fun setupRecyclerView() {
        adapter = SavedSnacksAdapter(
            onRemoveClick = { substitute -> removeSavedItem(substitute) }
        )
        binding.rvSavedSnacks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSavedSnacks.adapter = adapter
    }

    private fun fetchSavedSnacks() {
        Log.d("SavedSnacks", "🚀 fetchSavedSnacks() called")

        if (userId.isBlank()) {
            Log.e("SavedSnacks", "❌ User ID is blank. Cannot fetch.")
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val path = "users/$userId/saved_items"
        Log.d("SavedSnacks", "📡 Fetching from Firestore path: $path")

        firestore.collection("users")
            .document(userId)
            .collection("saved_items")
            .get()
            .addOnSuccessListener { result ->
                Log.d("SavedSnacks", "✅ Firestore returned ${result.size()} documents")

                if (result.isEmpty) {
                    Log.w("SavedSnacks", "⚠️ No saved items found for userId: $userId")
                    Toast.makeText(requireContext(), "No saved snacks found", Toast.LENGTH_SHORT).show()
                    binding.tvEmptyState.visibility = View.VISIBLE
                } else {
                    val savedList = result.documents.map { doc ->
                        Log.d("SavedSnacks", "📄 Document: ${doc.id} -> ${doc.data}")
                        Substitute.fromDocument(doc)
                    }
                    adapter.submitList(savedList)
                    binding.tvEmptyState.visibility = View.GONE
                }
            }
            .addOnFailureListener { exception ->
                Log.e("SavedSnacks", "❌ Firestore fetch failed: ${exception.message}", exception)
                Toast.makeText(requireContext(), "Error loading saved items", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeSavedItem(substitute: Substitute) {
        if (userId.isBlank()) {
            Log.e("SavedSnacks", "❌ Cannot remove item. User ID is blank.")
            return
        }

        Log.d("SavedSnacks", "🗑 Removing item: ${substitute.id}")

        firestore.collection("users")
            .document(userId)
            .collection("saved_items")
            .document(substitute.id)
            .delete()
            .addOnSuccessListener {
                Log.d("SavedSnacks", "✅ Successfully removed item: ${substitute.id}")
                Toast.makeText(requireContext(), "Item removed from saved snacks", Toast.LENGTH_SHORT).show()
                fetchSavedSnacks()
            }
            .addOnFailureListener { e ->
                Log.e("SavedSnacks", "❌ Failed to remove item: ${e.message}", e)
                Toast.makeText(requireContext(), "Failed to remove item", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
