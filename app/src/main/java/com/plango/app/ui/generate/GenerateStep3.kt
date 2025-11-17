package com.plango.app.ui.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.plango.app.databinding.FragmentGenerateStep3Binding
import com.plango.app.util.UiEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

class GenerateStep3 : Fragment () {
    private lateinit var binding: FragmentGenerateStep3Binding
    private val viewModel: GenerateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGenerateStep3Binding.inflate(inflater, container, false)

        val destination = viewModel.destination.value

        binding.nextButton.visibility = View.INVISIBLE
        binding.ivDestination.visibility = View.INVISIBLE
        lifecycleScope.launch {
            UiEffect.typeTextEffect(binding.tvMessage,"${destination} 여행을 가신다니, 좋은 선택이에요!\n\n", 40)

            delay(400)
            UiEffect.showWithFade(binding.ivDestination)
            delay(300)
            UiEffect.showWithFade(binding.nextButton)
        }

        binding.nextButton.setOnClickListener {
            requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            (activity as? GenerateActivity)?.moveToNextFragment(GenerateStep4())
        }

        return binding.root
    }
}