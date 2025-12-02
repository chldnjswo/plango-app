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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.plango.app.databinding.FragmentHomeStep1Binding
import com.plango.app.ui.generate.GenerateActivity
import com.plango.app.viewmodel.UserViewModel
import kotlinx.coroutines.launch

class HomeStep1 : Fragment() {

    private var _binding: FragmentHomeStep1Binding? = null
    private val binding get() = _binding!!

    private var selectedImageUri: Uri? = null

    private val userViewModel: UserViewModel by activityViewModels()

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    Glide.with(this)
                        .load(uri)
                        .centerCrop()
                        .into(binding.profileImage)

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

        // 화면 진입 시 유저 정보 Flow 자동 반영
        observeUserInfo()

        // ViewPager 설정
        val adapter = MyPageViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        val titles = listOf("다가 올 여행", "지난 여행")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = titles[pos]
        }.attach()

        // 여행 생성 버튼
        binding.btnCreateTrip.setOnClickListener {
            startActivity(Intent(requireContext(), GenerateActivity::class.java))
        }

        // 프로필 이미지 수정
        binding.profileImage.setOnClickListener {
            openGallery()
        }

        loadProfileImage()

        // 🔥 프로필 수정 Dialog 열기 (아이콘 클릭)
        binding.btnMenu.setOnClickListener {
            val user = userViewModel.userResponseFlow.value
            if (user != null) {
                HomeDialog(
                    publicId = user.publicId,
                    currentName = user.name,
                    currentMbti = user.mbti
                ).show(parentFragmentManager, "edit_profile")
            }
        }

    }

    private fun observeUserInfo() {
        lifecycleScope.launch {
            userViewModel.userResponseFlow.collect { user ->
                if (user != null) {
                    binding.tvUserName.text = user.name
                    // 필요 시 MBTI 표기 뷰가 있으면 반영
                    // binding.tvMbti.text = user.mbti
                }
            }
        }
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
