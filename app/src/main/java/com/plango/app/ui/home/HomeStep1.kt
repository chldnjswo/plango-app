package com.plango.app.ui.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.plango.app.ui.home.MyPageViewPagerAdapter
import com.plango.app.databinding.FragmentHomeStep1Binding
import com.google.android.material.tabs.TabLayoutMediator
import com.plango.app.ui.generate.GenerateActivity



class HomeStep1 : Fragment() {

    private var _binding: FragmentHomeStep1Binding? = null
    private val binding get() = _binding!!
    private var userName: String? = null
    private var selectedImageUri: Uri? =null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    Glide.with(this)
                        .load(uri)
                        .centerCrop()
                        .into(binding.profileImage)
                    // 선택된 이미지 임시 저장 (SharedPreferences)
                    saveProfileImage(uri)
                }
            }
        }


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

        //  ViewPager2 어댑터 설정
        val adapter = MyPageViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter
        userName = arguments?.getString("userName")
        android.util.Log.d("HomeStep1", " 프래그먼트로 전달된 userName=$userName")

        val titles = listOf("다가 올 여행", "지난 여행")
        binding.tvUserName.text = "${userName}"

        //  TabLayout + ViewPager 연결
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = titles[pos]
        }.attach()
        binding.btnCreateTrip.setOnClickListener {
            val intent = Intent(requireContext(), GenerateActivity::class.java)
            startActivity(intent)
        }
        binding.profileImage.setOnClickListener {
            openGallery()
        }
        loadProfileImage()
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        pickImageLauncher.launch(intent)
    }

    private fun saveProfileImage(uri: Uri) {
        val prefs = requireContext().getSharedPreferences("profile_prefs", Activity.MODE_PRIVATE)
        prefs.edit().putString("profile_uri", uri.toString()).apply()
    }

    private fun loadProfileImage() {
        val prefs = requireContext().getSharedPreferences("profile_prefs", Activity.MODE_PRIVATE)
        val uriString = prefs.getString("profile_uri", null)
        uriString?.let {
            Glide.with(this)
                .load(Uri.parse(it))
                .centerCrop()
                .into(binding.profileImage)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
