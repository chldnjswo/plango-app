package com.plango.app.ui.generate

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.plango.app.databinding.FragmentGenerateStep1Binding
import com.plango.app.ui.register.RegisterActivity
import com.plango.app.ui.register.RegisterStep2
import com.plango.app.util.UiEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GenerateStep1 : Fragment () {
    private lateinit var binding: FragmentGenerateStep1Binding
    private val viewModel: GenerateViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGenerateStep1Binding.inflate(inflater, container, false)

        binding.abroadButton.visibility = View.INVISIBLE
        binding.domesticButton.visibility = View.INVISIBLE
        lifecycleScope.launch {
            UiEffect.typeTextEffect(binding.tvMake, "\n여행 계획을 \n 만들어 볼까요?", 40)
            delay(600)
            UiEffect.typeTextEffect(binding.tvWhere, "\n\n\n\n 어디로 여행을 가실 예정인가요?", 40)

            delay(500)
            UiEffect.showWithFade(binding.domesticButton)
            delay(200)
            UiEffect.showWithFade(binding.abroadButton)
        }

        binding.domesticButton.setOnClickListener {
            viewModel.setTravelType("DOMESTIC") // ✅ ViewModel에 저장
            requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            (activity as? GenerateActivity)?.moveToNextFragment(GenerateStep2A())
        }


        binding.abroadButton.setOnClickListener {
            viewModel.setTravelType("OVERSEAS") // ✅ ViewModel에 저장
            requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            (activity as? GenerateActivity)?.moveToNextFragment(GenerateStep2B())
        }

        return binding.root
    }
}