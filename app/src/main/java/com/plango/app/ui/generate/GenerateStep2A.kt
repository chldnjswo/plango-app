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
import com.plango.app.databinding.FragmentGenerateStep2aBinding
import com.plango.app.util.UiEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

class GenerateStep2A : Fragment () {
    private lateinit var binding: FragmentGenerateStep2aBinding
    private val viewModel: GenerateViewModel by activityViewModels()
    private lateinit var adapter: DomesticAdapter

    private val domesticList = listOf(
        "서울", "부산", "대구", "제주도",
        "춘천", "강릉", "경주", "전주",
        "인천", "여수", "평창", "가평",
        "삼척", "안면도", "단양", "통영"
    ).map { DomesticItem(it) }.toMutableList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGenerateStep2aBinding.inflate(inflater, container, false)

        adapter = DomesticAdapter(requireContext(), domesticList) { selected ->
            domesticList.forEach { it.isSelected = it == selected }
            adapter.notifyDataSetChanged()
            viewModel.setDestination(selected.destination)
        }

        binding.recyclerDomestic.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerDomestic.adapter = adapter
        binding.recyclerDomestic.setHasFixedSize(true)

        binding.nextButton.visibility = View.INVISIBLE
        binding.recyclerDomestic.visibility = View.INVISIBLE
        lifecycleScope.launch {
            UiEffect.typeTextEffect(binding.tvDomesticWhere, "국내 어디로 가시겠어요?", 50)
            delay(800)
            UiEffect.typeTextEffect(binding.tvRecommend, "\n\n\n 추천 여행지는 다음과 같아요.", 50)

            delay(500)
            UiEffect.showWithFade(binding.recyclerDomestic)

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