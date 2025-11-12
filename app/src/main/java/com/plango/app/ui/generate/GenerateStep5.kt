package com.plango.app.ui.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.plango.app.databinding.FragmentGenerateStep5Binding
import kotlinx.coroutines.launch
import com.plango.app.util.UiEffect
import kotlinx.coroutines.delay

class GenerateStep5 : Fragment() {

    private var _binding: FragmentGenerateStep5Binding? = null
    private val binding get() = _binding!!

    private val viewModel: GenerateViewModel by activityViewModels()
    private lateinit var adapter: CompanionAdapter

    private val companionList = listOf(
        CompanionItem(CompanionItem.CompanionType.SOLO),
        CompanionItem(CompanionItem.CompanionType.COUPLE),
        CompanionItem(CompanionItem.CompanionType.FAMILY),
        CompanionItem(CompanionItem.CompanionType.FRIEND)
    ).toMutableList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenerateStep5Binding.inflate(inflater, container, false)

        binding.recyclerCompanion.visibility = View.INVISIBLE
        binding.btnNext.visibility = View.INVISIBLE
        lifecycleScope.launch {
            UiEffect.typeTextEffect(binding.tvQuestion, "누구와 함께 가시나요? 👥", 40)
            delay(500)
            UiEffect.typeTextEffect(binding.tvExplain, "\n\n\n 동반자를 선택해주세요!", 40)

            delay(500)
            UiEffect.showWithFade(binding.recyclerCompanion)

            delay(500)
            UiEffect.showWithFade(binding.btnNext)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = CompanionAdapter(requireContext(), companionList) { selected ->
            companionList.forEach { it.isSelected = it == selected }
            adapter.notifyDataSetChanged()
        }

        binding.recyclerCompanion.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerCompanion.adapter = adapter


        binding.btnNext.setOnClickListener {
            val selected = companionList.find { it.isSelected }?.companionType
            if (selected != null) {

                viewModel.setCompanionType(
                    when (selected) {
                        CompanionItem.CompanionType.SOLO -> GenerateViewModel.CompanionType.SOLO
                        CompanionItem.CompanionType.COUPLE -> GenerateViewModel.CompanionType.COUPLE
                        CompanionItem.CompanionType.FAMILY -> GenerateViewModel.CompanionType.FAMILY
                        CompanionItem.CompanionType.FRIEND -> GenerateViewModel.CompanionType.FRIEND
                    }
                )


                (activity as? GenerateActivity)?.moveToNextFragment(GenerateStep6())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
