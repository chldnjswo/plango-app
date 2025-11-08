package com.plango.app.ui.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.plango.app.databinding.FragmentGenerateStep2bBinding
import com.plango.app.util.UiEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

class GenerateStep2B : Fragment () {
    private lateinit var binding: FragmentGenerateStep2bBinding
    private val viewModel: GenerateViewModel by activityViewModels()
    private lateinit var adapter: AbroadAdapter

    private val abroadList = listOf(
        "도쿄", "오사카", "후쿠오카", "홋카이도",
        "상하이", "발리", "방콕", "베트남",
        "뉴욕", "캘리포니아", "프랑스", "이탈리아",
        "하와이", "스페인", "호주", "괌"
    ).map { AbroadItem(it) }.toMutableList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGenerateStep2bBinding.inflate(inflater, container, false)

        adapter = AbroadAdapter(requireContext(), abroadList) { selected ->
            abroadList.forEach { it.isSelected = it == selected }
            adapter.notifyDataSetChanged()
            viewModel.setDestination(selected.destination)
        }

        binding.recyclerAbroad.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerAbroad.adapter = adapter
        binding.recyclerAbroad.setHasFixedSize(true)

        binding.nextButton.visibility = View.INVISIBLE
        binding.recyclerAbroad.visibility = View.INVISIBLE
        lifecycleScope.launch {
            UiEffect.typeTextEffect(binding.tvAbroadWhere, "해외 어디로 가시겠어요?", 50)
            delay(800)
            UiEffect.typeTextEffect(binding.tvRecommend, "\n\n\n 추천 여행지는 다음과 같아요.", 50)

            delay(500)
            UiEffect.showWithFade(binding.recyclerAbroad)

            delay(500)
            UiEffect.showWithFade(binding.nextButton)
        }

        viewModel.destination.observe(viewLifecycleOwner) { destination ->
            binding.nextButton.isEnabled = !destination.isNullOrEmpty()
        }

        binding.nextButton.setOnClickListener {
            val destination = viewModel.destination.value

            if (destination.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "목적지를 선택해주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.setDestination(destination)

            requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            (activity as? GenerateActivity)?.moveToNextFragment(GenerateStep3())
        }

        return binding.root
    }
}