package com.lavisha.firstapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lavisha.firstapplication.databinding.ItemSavedSnackBinding

class SavedSnacksAdapter(
    private val onRemoveClick: (Substitute) -> Unit
) : ListAdapter<Substitute, SavedSnacksAdapter.SavedSnacksViewHolder>(SubstituteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedSnacksViewHolder {
        val binding = ItemSavedSnackBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SavedSnacksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedSnacksViewHolder, position: Int) {
        val substitute = getItem(position)
        holder.bind(substitute)
    }

    inner class SavedSnacksViewHolder(private val binding: ItemSavedSnackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(substitute: Substitute) {
            binding.tvSubstituteName.text = substitute.name
            binding.tvSubstituteBenefits.text = substitute.benefits
            binding.tvSubstituteCalories.text = "Calories: ${substitute.calories}"
            binding.tvSubstituteProtein.text = "Protein: ${substitute.protein}"

            // Set the remove button click listener
            binding.btnRemove.setOnClickListener {
                onRemoveClick(substitute)
            }
        }
    }
}
