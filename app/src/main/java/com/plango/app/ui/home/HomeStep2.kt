package com.plango.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.plango.app.R
import com.plango.app.data.travel.Trip

class HomeStep2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_step2, container, false)

        // 1️⃣ RecyclerView 연결
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerTrip)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 2️⃣ 예시용 여행 데이터 (Dummy)
        val tripList = listOf(
            Trip("서울 여행", "2024.07.01 - 2024.07.05", R.drawable.sample_travel_photo),
            Trip("경주 여행", "2024.09.10 - 2024.09.13", R.drawable.sample_travel_photo),
            Trip("인천 여행", "2024.10.02 - 2024.10.04", R.drawable.sample_travel_photo)
        )

        // 3️⃣ Adapter 연결
        val adapter = TripAdapter(tripList)
        recyclerView.adapter = adapter

        return view
    }
}
