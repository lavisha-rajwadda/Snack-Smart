package com.lavisha.firstapplication

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    // Your fragments
    private val fragmentList = listOf(
        HomeFragment(),              // 🏠 Home
        SavedSnacksFragment()       // 💾 My Saved Snacks
    )

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}
