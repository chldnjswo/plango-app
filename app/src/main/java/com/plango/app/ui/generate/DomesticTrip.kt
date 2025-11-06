package com.example.plango_nickname

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.plango_nickname.databinding.ActivityDomesticTripBinding
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson

class DomesticTrip : AppCompatActivity() {
    private lateinit var binding: ActivityDomesticTripBinding
    private var userName: String = ""
    private var selectedTrip: MaterialButton? = null
    private var selectedCity: String? = null

    private val domesticButtons by lazy {
        listOf(
            binding.btnSeoul, binding.btnBusan, binding.btnDaegu, binding.btnJeju,
            binding.btnChuncheon, binding.btnGyeongju, binding.btnJeonju, binding.btnGangneung
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDomesticTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("userName").orEmpty()


        setupAutoCompleteFromJson()


        domesticButtons.forEach { button ->
            button.setOnClickListener {

                if (selectedTrip == button) {
                    deselectButton(button)
                    selectedTrip = null
                    binding.etSearchTrip.isEnabled = true
                    binding.etSearchTrip.text.clear()
                    selectedCity = null
                } else {
                    // 새 버튼 클릭 시 이전 버튼 해제
                    selectedTrip?.let { deselectButton(it) }
                    updateSelectedButton(button)
                    selectedTrip = button
                    selectedCity = button.text.toString()

                    // 입력창 비활성화
                    binding.etSearchTrip.isEnabled = false
                    binding.etSearchTrip.text.clear()
                }
            }
        }


        binding.etSearchTrip.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    domesticButtons.forEach { it.isEnabled = false }
                    selectedTrip?.let { deselectButton(it) }
                    selectedTrip = null
                } else {
                    domesticButtons.forEach { it.isEnabled = true }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.nextButton.setOnClickListener {
            if (selectedCity != null) {
                val intent = Intent(this, SelectDay::class.java)
                intent.putExtra("userName", userName)
                intent.putExtra("selectedCity", selectedCity)
                startActivity(intent)

            }
            else{
                Toast.makeText(this, "도시를 선택한 후 다음 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupAutoCompleteFromJson() {
        try {
            val inputStream = resources.openRawResource(R.raw.cities)
            val json = inputStream.bufferedReader().use { it.readText() }
            val cities = Gson().fromJson(json, Array<String>::class.java).toList()

            val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, cities)
            binding.etSearchTrip.setAdapter(adapter)


            binding.etSearchTrip.setOnItemClickListener { parent, _, position, _ ->
                val city = parent.getItemAtPosition(position).toString()
                binding.etSearchTrip.setText(city)
                selectedCity = city


                domesticButtons.forEach { it.isEnabled = false }
                selectedTrip?.let { deselectButton(it) }
                selectedTrip = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun updateSelectedButton(selectedButton: MaterialButton) {
        val colorSelectedBg = ContextCompat.getColor(this, R.color.button_primary)
        val colorSelectedText = ContextCompat.getColor(this, android.R.color.white)

        selectedButton.backgroundTintList = ColorStateList.valueOf(colorSelectedBg)
        selectedButton.setTextColor(colorSelectedText)
        selectedButton.strokeWidth = 0
        selectedButton.animate().scaleX(1.08f).scaleY(1.08f).setDuration(120).start()
    }


    private fun deselectButton(button: MaterialButton) {
        val colorUnselectedBg = ContextCompat.getColor(this, R.color.card_background)
        val colorUnselectedText = ContextCompat.getColor(this, R.color.text_primary)
        val colorStroke = ContextCompat.getColor(this, R.color.card_border)

        button.backgroundTintList = ColorStateList.valueOf(colorUnselectedBg)
        button.setTextColor(colorUnselectedText)
        button.strokeColor = ColorStateList.valueOf(colorStroke)
        button.strokeWidth = 2
        button.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
    }
}
