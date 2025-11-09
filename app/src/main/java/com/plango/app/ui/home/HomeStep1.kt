package com.plango.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.plango.app.ui.home.MyPageViewPagerAdapter
import com.plango.app.databinding.FragmentHomeStep1Binding
import com.google.android.material.tabs.TabLayoutMediator
import com.plango.app.ui.generate.GenerateActivity

class HomeStep1 : Fragment() {

    private var _binding: FragmentHomeStep1Binding? = null
    private val binding get() = _binding!!
    private var userName: String? = null


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
        userName = arguments?.getString("userName")
        android.util.Log.d("HomeStep1", "✅ 프래그먼트로 전달된 userName=$userName")

        val titles = listOf("다가 올 여행", "지난 여행")
        binding.tvUserName.text = "${userName}"

        // 🔹 TabLayout + ViewPager 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = titles[pos]
        }.attach()
        binding.btnCreateTrip.setOnClickListener {
            val intent = Intent(requireContext(), GenerateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
