package com.lavisha.firstapplication

import androidx.recyclerview.widget.DiffUtil

class SubstituteDiffCallback : DiffUtil.ItemCallback<Substitute>() {
    override fun areItemsTheSame(oldItem: Substitute, newItem: Substitute): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Substitute, newItem: Substitute): Boolean {
        return oldItem == newItem
    }
}
