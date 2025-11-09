package com.plango.app.ui.generate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.plango.app.databinding.FragmentGenerateStep4Binding
import java.text.SimpleDateFormat
import java.util.*

class GenerateStep4 : Fragment() {

    private var _binding: FragmentGenerateStep4Binding? = null
    private val binding get() = _binding!!

    private val viewModel: GenerateViewModel by activityViewModels()
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenerateStep4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.etStartDate.setOnClickListener {
            showDatePicker { selectedDate ->
                binding.etStartDate.setText(selectedDate)
                viewModel.setStartDate(selectedDate)
            }
        }

        binding.etEndDate.setOnClickListener {
            showDatePicker { selectedDate ->
                binding.etEndDate.setText(selectedDate)
                viewModel.setEndDate(selectedDate)
            }
        }


        binding.btnNext.setOnClickListener {
            val start = viewModel.startDate.value
            val end = viewModel.endDate.value

            if (start.isNullOrEmpty() || end.isNullOrEmpty()) {
                com.google.android.material.snackbar.Snackbar.make(
                    binding.root,
                    "여행 날짜를 모두 선택해주세요!",
                    com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            (activity as? GenerateActivity)?.moveToNextFragment(GenerateStep5())
        }
    }


    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val constraints = CalendarConstraints.Builder()
            .setOpenAt(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("날짜 선택")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setCalendarConstraints(constraints)
            .build()

        picker.addOnPositiveButtonClickListener { millis ->
            val date = Date(millis)
            onDateSelected(dateFormatter.format(date))
        }

        picker.show(parentFragmentManager, "datePicker")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
