package com.plango.app.ui.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.plango.app.databinding.FragmentRegisterStep2Binding
import com.plango.app.ui.PageLoading
import com.plango.app.util.UiEffect
import com.plango.app.viewmodel.RegisterViewModel
import com.plango.app.viewmodel.UserViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.getValue

class RegisterStep2 : Fragment() {

    private lateinit var binding: FragmentRegisterStep2Binding
    private val viewModel: RegisterViewModel by activityViewModels {
        defaultViewModelProviderFactory
    }
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var adapter: MbtiAdapter
    private val mbtiList = listOf(
        "ISTJ", "ISFJ", "INFJ", "INTJ",
        "ISTP", "ISFP", "INFP", "INTP",
        "ESTP", "ESFP", "ENFP", "ENTP",
        "ESTJ", "ESFJ", "ENFJ", "ENTJ"
    ).map { MbtiItem(it) }.toMutableList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterStep2Binding.inflate(inflater, container, false)

        adapter = MbtiAdapter(requireContext(), mbtiList) { selected ->
            mbtiList.forEach { it.isSelected = it == selected }
            viewModel.setMbti(selected.type)
            adapter.notifyDataSetChanged()
        }

        binding.recyclerMbti.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerMbti.adapter = adapter

        binding.recyclerMbti.visibility = View.INVISIBLE
        binding.nextButton.visibility = View.INVISIBLE


         lifecycleScope.launch {
            val name = viewModel.nickname.value ?: "사용자"
            UiEffect.typeTextEffect(binding.tvQuestion, "$name 님에 대해서 궁금해요 🤔")
            delay(600)
            UiEffect.typeTextEffect(binding.tvExplain, "MBTI가 어떻게 되시나요?")
            delay(600)
            UiEffect.showWithFade(binding.recyclerMbti)
            delay(200)
            UiEffect.showWithFade(binding.nextButton)
        }

        viewModel.mbti.observe(viewLifecycleOwner) { mbti ->
            binding.nextButton.isEnabled = !mbti.isNullOrEmpty()
        }


        binding.nextButton.setOnClickListener {
            val mbti = viewModel.mbti.value
            val name = viewModel.nickname.value ?: ""
            Log.d("RegisterStep2", "mbti: $mbti, name: $name")


            if (mbti.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "MBTI를 선택해주세요!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = Intent(requireContext(), PageLoading::class.java).apply{
                putExtra("userName", name)
                putExtra("mbti", mbti)
                putExtra("mode", "user")
            }
            startActivity(intent)

            requireActivity().overridePendingTransition(
                android.R.anim.slide_in_left, android.R.anim.slide_out_right
            )

        }

        return binding.root
    }
}
