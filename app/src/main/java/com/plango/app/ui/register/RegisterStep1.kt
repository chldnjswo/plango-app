package com.plango.app.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.plango.app.databinding.FragmentRegisterStep1Binding
import com.plango.app.ui.register.RegisterActivity
import com.plango.app.util.UiEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterStep1 : Fragment() {

    private lateinit var binding: FragmentRegisterStep1Binding
    private val viewModel: RegisterViewModel by activityViewModels()  // ✅ Acivity 범위 공유 ViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterStep1Binding.inflate(inflater, container, false)

        // 처음엔 입력창 숨기기
        binding.nicknameInput.visibility = View.INVISIBLE
        binding.nextButton.visibility = View.INVISIBLE

        // 타이핑 효과 + 페이드인 효과 실행
        lifecycleScope.launch {
            UiEffect.typeTextEffect(binding.tvGreeting, "안녕하세요 👋\n만나서 반가워요.", 55)
            delay(800)
            UiEffect.typeTextEffect(binding.tvIntro, "저는 떠날지도의 AI 여행비서예요!", 55)
            delay(800)
            UiEffect.typeTextEffect(binding.tvExplain, "사용자님에 대해서 몇 가지\n알고 싶어요! 📝", 55)
            delay(800)
            UiEffect.typeTextEffect(binding.tvQuestion, "성함이 어떻게 되시나요?", 55)

            delay(600)
            UiEffect.showWithFade(binding.nicknameInput)
            delay(200)
            UiEffect.showWithFade(binding.nextButton)
        }

        // “다음” 버튼 클릭 시
        binding.nextButton.setOnClickListener {
            val name = binding.nicknameInput.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "사용하실 이름을 입력해주세요!", Toast.LENGTH_SHORT).show()
            } else {
                // ✅ ViewModel에 닉네임 저장
                viewModel.setNickname(name)

                // ✅ 다음 프래그먼트로 이동
                requireActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                (activity as? RegisterActivity)?.moveToNextFragment(RegisterStep2())
            }
        }

        return binding.root
    }
}
