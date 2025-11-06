package com.example.plango_nickname

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPageViewPagerAdapter(activity: FragmentActivity): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int =2
    override fun createFragment(position: Int):Fragment {
        return when(position) {

            0-> UpComingTrip()
            else-> PastTrip()
        }
    }
}