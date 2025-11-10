package com.plango.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.plango.app.R
import com.plango.app.data.travel.Trip
import com.plango.app.databinding.FragmentHomeStep3Binding

class HomeStep3 : Fragment() {
    private var _binding: FragmentHomeStep3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeStep3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ✅ 지난 여행 더미 데이터
        val tripList = listOf(
            Trip("제주 여행", "2023.04.02 - 2023.04.06", R.drawable.sample_travel_photo),
            Trip("부산 여행", "2023.06.10 - 2023.06.13", R.drawable.sample_travel_photo),
            Trip("여수 여행", "2023.08.12 - 2023.08.15", R.drawable.sample_travel_photo)
        )

        // ✅ 리사이클러뷰 연결
        binding.recyclerTripPast.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTripPast.adapter = TripAdapter(tripList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
