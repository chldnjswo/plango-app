package com.plango.app.ui.generate

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.plango.app.data.user.UserPrefs
import com.plango.app.databinding.FragmentGenerateStep6Binding
import com.plango.app.ui.PageLoading
import kotlinx.coroutines.launch
import com.plango.app.util.UiEffect
import kotlinx.coroutines.delay

class GenerateStep6 : Fragment() {

    private var _binding: FragmentGenerateStep6Binding? = null
    private val binding get() = _binding!!
    private val viewModel: GenerateViewModel by activityViewModels()

    private lateinit var adapter: ThemeAdapter
    private val themeList = mutableListOf(
        ThemeItem("힐링"), ThemeItem("맛집"), ThemeItem("쇼핑"),
        ThemeItem("사진"), ThemeItem("감성"), ThemeItem("액티비티"),
        ThemeItem("가성비"),ThemeItem("럭셔리"), ThemeItem("축제"), ThemeItem("레트로"),
        ThemeItem("카페투어"), ThemeItem("술집탐방"), ThemeItem("야경맛집"),
        ThemeItem("지금 뜨는 곳")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenerateStep6Binding.inflate(inflater, container, false)

        binding.recyclerTheme.visibility = View.INVISIBLE
        binding.btnNext.visibility = View.INVISIBLE
        lifecycleScope.launch {
            UiEffect.typeTextEffect(binding.tvQuestion, "어떤 테마의 여행을 원하시나요? \uD83C\uDFA8", 40)
            delay(500)
            UiEffect.typeTextEffect(binding.tvExplain, "\n\n\n 3개를 선택해주세요!", 40)

            delay(500)
            UiEffect.showWithFade(binding.recyclerTheme)

            delay(500)
            UiEffect.showWithFade(binding.btnNext)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ThemeAdapter(requireContext(), themeList) { selected ->
            val selectedCount = themeList.count { it.isSelected }

            if (selected.isSelected) {
                selected.isSelected = false
            } else if (selectedCount < 3) {
                selected.isSelected = true
            }

            adapter.notifyDataSetChanged()
        }

        binding.recyclerTheme.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerTheme.adapter = adapter


        binding.btnNext.setOnClickListener {
            val selectedThemes = themeList.filter { it.isSelected }.map { it.theme }

            if (selectedThemes.size!=3) {
                com.google.android.material.snackbar.Snackbar.make(
                    binding.root,
                    "세 개의 테마를 선택해주세요!",
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            viewModel.setThemes(selectedThemes)

            lifecycleScope.launch {
                val userId = UserPrefs.getUserIdOnce(requireContext())
                if(userId.isNullOrEmpty()){
                    Snackbar.make(binding.root, "유저 정보를 불러올 수 없습니다.", Snackbar.LENGTH_SHORT).show()
                    return@launch
                }
                val intent = Intent(requireContext(), PageLoading::class.java).apply{
                    putExtra("mode", "travel")
                    putExtra("userPublicId", userId)
                    putExtra("travelType", viewModel.travelType.value?: "DOMESTIC") // or OVERSEAS
                    putExtra("travelDest", viewModel.destination.value ?: "서울")
                    putExtra("startDate", viewModel.startDate.value ?: "")
                    putExtra("endDate", viewModel.endDate.value ?: "")
                    putStringArrayListExtra("themes", ArrayList(selectedThemes))
                    putExtra("companionType", viewModel.companionType.value?.name ?: "SOLO")
                }
                startActivity(intent)

            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
