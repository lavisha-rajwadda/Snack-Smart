package com.lavisha.firstapplication

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class SubstituteAdapter(
    private val onSaveClick: (Substitute) -> Unit,
    private val onItemClick: (Substitute) -> Unit
) : RecyclerView.Adapter<SubstituteAdapter.ViewHolder>() {

    private var substitutes = emptyList<Substitute>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvSubstituteName)
        val benefits: TextView = itemView.findViewById(R.id.tvBenefits)
        val calories: TextView = itemView.findViewById(R.id.tvCalories)
        val protein: TextView = itemView.findViewById(R.id.tvProtein)
        val saveButton: MaterialButton = itemView.findViewById(R.id.btnRemove)

        fun bind(substitute: Substitute) {
            name.text = substitute.name
            benefits.text = substitute.benefits
            calories.text = "${substitute.calories} cal"
            protein.text = "${substitute.protein}g protein"

            updateSaveButton(substitute.isSaved)

            // Save button click
            saveButton.setOnClickListener {
                onSaveClick(substitute)
            }

            // Entire item click
            itemView.setOnClickListener {
                onItemClick(substitute)
            }
        }

        private fun updateSaveButton(isSaved: Boolean) {
            saveButton.apply {
                text = if (isSaved) "Saved" else "Save"
                setIconResource(if (isSaved) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_outline)
                iconTint = ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context,
                        if (isSaved) R.color.savedColor else R.color.primaryColor)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_substitute, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(substitutes[position])
    }

    override fun getItemCount() = substitutes.size

    fun submitList(newList: List<Substitute>) {
        substitutes = newList
        notifyDataSetChanged()
    }

    fun updateItemSavedState(id: String, saved: Boolean) {
        val index = substitutes.indexOfFirst { it.id == id }
        if (index != -1) {
            substitutes = substitutes.toMutableList().apply {
                this[index] = this[index].copy(isSaved = saved)
            }
            notifyItemChanged(index)
        }
    }
}
