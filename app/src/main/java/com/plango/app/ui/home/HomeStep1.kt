package com.plango.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.plango.app.ui.home.MyPageViewPagerAdapter
import com.plango.app.databinding.FragmentHomeStep1Binding
import com.google.android.material.tabs.TabLayoutMediator

class HomeStep1 : Fragment() {

    private var _binding: FragmentHomeStep1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeStep1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔹 ViewPager2 어댑터 설정
        val adapter = MyPageViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        val titles = listOf("다가 올 여행", "지난 여행")

        // 🔹 TabLayout + ViewPager 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = titles[pos]
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
