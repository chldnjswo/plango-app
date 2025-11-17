package com.plango.app.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPageViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeStep2() // 다가 올 여행
            1 -> HomeStep3() // 지난 여행
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
