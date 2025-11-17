package com.plango.app.ui.generate

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        setupRecycler()
        setupAutoComplete()
        setupFadeAnimation()
        setupNextButton()

        return binding.root
    }

    private fun setupRecycler() {
        adapter = AbroadAdapter(requireContext(), abroadList) { selected ->
            if (selected.isSelected) {
                selected.isSelected = false
                viewModel.setDestination("")
                binding.nextButton.isEnabled = false

                binding.etSearchTrip.apply {
                    isEnabled = true
                }

                binding.recyclerAbroad.alpha = 1f
                binding.recyclerAbroad.isEnabled = true

                adapter.notifyDataSetChanged()
                return@AbroadAdapter
            }

            abroadList.forEach { it.isSelected = it == selected }
            adapter.notifyDataSetChanged()

            // Recycler 선택 시: 입력창 초기화 + 비활성화
            binding.etSearchTrip.apply {
                text.clear()
                isEnabled = false
            }

            viewModel.setDestination(selected.destination)
            binding.nextButton.isEnabled = true
        }

        binding.recyclerAbroad.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerAbroad.adapter = adapter
    }

    private fun setupAutoComplete() {

        binding.etSearchTrip.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    // 입력 시: 리사이클러뷰 비활성화
                    abroadList.forEach { it.isSelected = false }
                    adapter.notifyDataSetChanged()
                    binding.recyclerAbroad.alpha = 0.4f
                    binding.recyclerAbroad.isEnabled = false
                    viewModel.setDestination(s.toString())
                    binding.nextButton.isEnabled = true
                } else {
                    // 입력창 비면 다시 활성화
                    binding.recyclerAbroad.alpha = 1f
                    binding.recyclerAbroad.isEnabled = true
                    binding.nextButton.isEnabled = false
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.etSearchTrip.setOnItemClickListener { parent, _, position, _ ->
            val city = parent.getItemAtPosition(position).toString()
            binding.etSearchTrip.setText(city)
            viewModel.setDestination(city)
            binding.nextButton.isEnabled = true

            // 리사이클러뷰 비활성화
            binding.recyclerAbroad.alpha = 0.4f
            binding.recyclerAbroad.isEnabled = false
        }
    }

    private fun setupFadeAnimation() {
        binding.nextButton.visibility = View.INVISIBLE
        binding.recyclerAbroad.visibility = View.INVISIBLE
        binding.searchContainer.visibility = View.INVISIBLE

        lifecycleScope.launch {
            UiEffect.typeTextEffect(binding.tvAbroadWhere, "해외 어디로 가시겠어요?", 40)
            delay(600)
            UiEffect.typeTextEffect(binding.tvRecommend, "\n\n\n추천 여행지는 다음과 같아요.", 40)
            delay(400)
            UiEffect.showWithFade(binding.recyclerAbroad)
            delay(400)
            UiEffect.showWithFade(binding.searchContainer)
            delay(400)
            UiEffect.showWithFade(binding.nextButton)
        }
    }

    private fun setupNextButton() {
        binding.nextButton.setOnClickListener {
            val destination = viewModel.destination.value
            if (destination.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "목적지를 선택하거나 입력해주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.setDestination(destination)
            (activity as? GenerateActivity)?.moveToNextFragment(GenerateStep3())
        }
    }
}