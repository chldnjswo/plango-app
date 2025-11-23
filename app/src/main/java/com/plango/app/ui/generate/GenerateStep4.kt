package com.plango.app.ui.generate

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.plango.app.R
import com.plango.app.databinding.FragmentGenerateStep4Binding
import java.text.SimpleDateFormat
import java.util.*

class GenerateStep4 : Fragment() {

    private var _binding: FragmentGenerateStep4Binding? = null
    private val binding get() = _binding!!

    private val viewModel: GenerateViewModel by activityViewModels()

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    private var startDateMillis: Long? = null
    private var endDateMillis: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenerateStep4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etStartDate.setOnClickListener { showStartPicker() }
        binding.etEndDate.setOnClickListener { showEndPicker() }

        binding.btnNext.setOnClickListener {
            if (startDateMillis == null || endDateMillis == null) {
                Snackbar.make(binding.root, "여행 날짜를 선택해주세요!", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ViewModel 저장
            viewModel.setStartDate(formatter.format(Date(startDateMillis!!)))
            viewModel.setEndDate(formatter.format(Date(endDateMillis!!)))

            (activity as? GenerateActivity)
                ?.moveToNextFragment(GenerateStep5())
        }
    }

    /**
     * 시작일 DatePicker
     */
    private fun showStartPicker() {
        val cal = Calendar.getInstance()

        val dialog = DatePickerDialog(
            requireContext(),
            R.style.MyDatePickerTheme,
            { _, y, m, d ->
                cal.set(y, m, d, 0, 0, 0)

                startDateMillis = cal.timeInMillis
                binding.etStartDate.setText(formatter.format(cal.time))

                endDateMillis = null
                binding.etEndDate.setText("")
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )

        // 💡 show() 이후 버튼 색 강제 적용
        dialog.setOnShowListener {
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#E07559"))
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#E07559"))
        }

        dialog.show()
    }

    /**
     * 종료일 DatePicker (+ 최대 14일 제한)
     */
    private fun showEndPicker() {
        if (startDateMillis == null) {
            Snackbar.make(binding.root, "먼저 여행 시작일을 선택해주세요!", Snackbar.LENGTH_SHORT).show()
            return
        }

        val startCal = Calendar.getInstance().apply { timeInMillis = startDateMillis!! }
        val endCal = Calendar.getInstance().apply { timeInMillis = startDateMillis!! }

        val maxCal = Calendar.getInstance().apply {
            timeInMillis = startDateMillis!! + (13L * 24 * 60 * 60 * 1000)
        }

        val dialog = DatePickerDialog(
            requireContext(),
            R.style.MyDatePickerTheme,
            { _, y, m, d ->
                endCal.set(y, m, d, 0, 0, 0)

                if (endCal.timeInMillis > maxCal.timeInMillis) {
                    Snackbar.make(
                        binding.root,
                        "여행 종료일은 시작일 기준 최대 14일까지 가능합니다.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                    return@DatePickerDialog
                }

                endDateMillis = endCal.timeInMillis
                binding.etEndDate.setText(formatter.format(endCal.time))
            },
            endCal.get(Calendar.YEAR),
            endCal.get(Calendar.MONTH),
            endCal.get(Calendar.DAY_OF_MONTH)
        )

        dialog.datePicker.minDate = startCal.timeInMillis
        dialog.datePicker.maxDate = maxCal.timeInMillis

        dialog.setOnShowListener {
            dialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(Color.parseColor("#E07559"))
            dialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(Color.parseColor("#E07559"))
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
