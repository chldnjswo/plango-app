package com.plango.app.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.plango.app.R
import com.plango.app.databinding.FragmentHomeDialogBinding
import com.plango.app.viewmodel.UserViewModel

class HomeDialog(
    private val publicId: String,
    private val currentName: String,
    private val currentMbti: String
) : DialogFragment() {

    private lateinit var binding: FragmentHomeDialogBinding
    private val userViewModel: UserViewModel by activityViewModels()
    private val mbtiList = listOf(
        "ISTJ", "ISFJ", "INFJ", "INTJ",
        "ISTP", "ISFP", "INFP", "INTP",
        "ESTP", "ESFP", "ENFP", "ENTP",
        "ESTJ", "ESFJ", "ENFJ", "ENTJ"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⭐ 가장 핵심: 전체 화면 Material Dialog로 스타일 강제
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullscreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etNickname.setText(currentName)
        binding.etMbti.setText(currentMbti)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mbtiList)
        (binding.etMbti as AutoCompleteTextView).setAdapter(adapter)
        binding.topAppBar.setNavigationOnClickListener {
            dismiss()
        }

        // 선택 시 텍스트 변경
        binding.etMbti.setOnItemClickListener { _, _, position, _ ->
            val selected = mbtiList[position]
            binding.etMbti.setText(selected)
        }

        // 저장 버튼
        binding.btnSave.setOnClickListener {
            val newName = binding.etNickname.text.toString()
            val newMbti = binding.etMbti.text.toString()

            userViewModel.updateUser(publicId, newName, newMbti)
            dismiss()
        }

    }
}
